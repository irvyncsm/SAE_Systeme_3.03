import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;


    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client, this);
                synchronized (connections) {
                    connections.add(handler);
                }
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String senderName, String message) {
        synchronized (connections) {
            for (ConnectionHandler handler : connections) {
                if (handler != null && handler.getName() != null && !handler.getName().equals(senderName)) {
                    handler.sendMessage(message);
                    System.out.println(senderName + " à envoyé : " + message + " à " + handler.getName());
                }
            }
        }
    }
    
    public void shutdown() {
        try {
            done = true;
            if (!server.isClosed()) {
                server.close();
            }
            synchronized (connections) {
                for (ConnectionHandler handler : connections) {
                    handler.shutdown();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeConnection(ConnectionHandler handler) {
        synchronized (connections) {
            connections.remove(handler);
        }
    }

    class ConnectionHandler implements Runnable {
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String name;
        private Server server;

        public ConnectionHandler(Socket clientSocket, Server server) {
            this.client = clientSocket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Entrez votre nom: ");
                String nameData = in.readLine();
                // récupérer le nom de l'utilisateur dans la réponse JSON du client
                // ex: {"id": 0, "user": "", "content": "irvyn", "date": "2024-01-20T16:25:41Z", "likes": 0}
                name = nameData.substring(nameData.indexOf("content") + 10, nameData.indexOf("date") - 3);
                // enlever les guillemets autour du nom (ex: "toto" -> toto)
                name = name.substring(1, name.length() - 1);
                System.out.println("New connection from " + name);
                broadcast(name, name + " est connecté.");
                String readLine;

                while ((readLine = in.readLine()) != null) {
                    String line = readLine.substring(readLine.indexOf("content") + 10, readLine.indexOf("date") - 3);
                    line = line.substring(1, line.length() - 1);
                    if (line.startsWith("/nick")) {
                        String[] messageSplit = line.split(" ", 2);
                        if (messageSplit.length == 2) {
                            boolean renamed = false;
                            String newName = messageSplit[1];
                            if (newName.equals(name)) {
                                out.println("Votre nom est déjà " + newName);
                                continue;
                            } else if (newName.isEmpty()) {
                                out.println("Votre nom ne peut pas être vide.");
                                continue;
                            } else {
                                for (ConnectionHandler handler : connections) {
                                    if (handler != null && handler.getName() != null && handler.getName().equals(newName)) {
                                        out.println("Ce nom est déjà pris.");
                                        renamed = true;
                                        break;
                                    }
                                }
                            }
                            if (!renamed){
                                broadcast(name, name + " a changé son nom en " + newName);
                                System.out.println(name + " a changé son nom en " + newName);
                                name = newName;
                                out.println("Votre nom a été changé en " + newName);
                            }
                        } else {
                            out.println("Commande Invalide. Usage: /nick <new name>");
                        }
                    } else if (line.startsWith("/quit")) {
                        broadcast(name, name + " vient de se déconnecter.");
                        server.removeConnection(this);
                        break;
                    } else if (line.startsWith("/list")) {
                        out.println("Liste des utilisateurs connectés: ");
                        for (ConnectionHandler handler : connections) {
                            if (handler != null && handler.getName() != null) {
                                out.println(handler.getName());
                            }
                        }
                    } else if (line.startsWith("/follow")) {
                        String[] messageSplit = line.split(" ", 2);
                        if (messageSplit.length == 2) {
                            String followName = messageSplit[1];
                            boolean found = false;
                            if (followName.equals(name)) {
                                out.println("Vous ne pouvez pas vous suivre vous-même.");
                                continue;
                            }
                            for (ConnectionHandler handler : connections) {
                                if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                                    out.println("Vous suivez maintenant " + followName);
                                    found = true;
                                    handler.sendMessage(name + " vous suit maintenant.");
                                }
                            }
                            if (!found) {
                                out.println("Utilisateur non trouvé.");
                            }
                        } else {
                            out.println("Commande Invalide. Usage: /follow <name>");
                        }
                    } else if (line.startsWith("/unfollow")) {
                        String[] messageSplit = line.split(" ", 2);
                        if (messageSplit.length == 2) {
                            String followName = messageSplit[1];
                            boolean found = false;
                            for (ConnectionHandler handler : connections) {
                                if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                                    out.println("Vous ne suivez plus " + followName);
                                    found = true;
                                    handler.sendMessage(name + " ne vous suit plus.");
                                }
                            }
                            if (!found) {
                                out.println("Utilisateur non trouvé.");
                            }
                        } else {
                            out.println("Commande Invalide. Usage: /unfollow <name>");
                        }
                    } else if (line.startsWith("/like") || line.equals("like ")) {
                        out.println("Fonctionnalité /like <id_message> non implémentée.");
                    } else if (line.startsWith("/unlike") || line.equals("unlike ")) {
                        out.println("Fonctionnalité /unlike <id_message> non implémentée.");
                    } else if (line.startsWith("/delete") || line.equals("delete ")) {
                        out.println("Fonctionnalité /delete <id_message> non implémentée.");
                    } else if (line.startsWith("/help") || line.equals("help ")) {
                        out.println("");
                        out.println("Commandes disponibles: ");
                        out.println("--> /nick <new name> : Change votre nom.");
                        out.println("--> /list : Liste les utilisateurs connectés.");
                        out.println("--> /follow <name> : Vous permet de suivre un utilisateur.");
                        out.println("--> /unfollow <name> : Vous permet de ne plus suivre un utilisateur.");
                        out.println("--> /like <id_message> : Vous permet de liker un message.");
                        out.println("--> /unlike <id_message> : Vous permet de unliker un message.");
                        out.println("--> /delete <id_message> : Vous permet de supprimer un message.");
                        out.println("--> /quit : Quitte le chat.");
                    } else {
                        broadcast(name, name + ": " + line);
                    }
                }
            } catch (IOException e) {
                // Ici, on gère la déconnexion inattendue
                server.broadcast(name, name + " s'est déconnecté de manière inattendue.");
            } finally {
                shutdown();
            }
        }

        public String getName() {
            return name;
        }

        public void sendMessage(String message){
            out.println(message);
        }

        public void shutdown(){
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
                synchronized (connections) {
                    connections.remove(this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}