import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ConnectionHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    private Server server;
    private List<ConnectionHandler> connections;
    private List<String> listeFollowers;
    private List<String> listeFollowings;

    public ConnectionHandler(Socket clientSocket, Server server) {
        this.client = clientSocket;
        this.server = server;
        this.connections = server.getConnections();
        this.listeFollowers = new ArrayList<>();
        this.listeFollowings = new ArrayList<>();
    }

    public int getNombreFollowers() {
        return listeFollowers.size();
    }

    public int getNombreFollowings() {
        return listeFollowings.size();
    }

    public void ajouterFollower(String client){
        listeFollowers.add(client);
    }

    public void ajouterFollowing(String client){
        listeFollowings.add(client);
    }

    public void retirerFollower(String client){
        listeFollowers.remove(client);
    }

    public void retirerFollowing(String client){
        listeFollowings.remove(client);
    }

    public List<String> getListeFollowers() {
        return listeFollowers;
    }

    public List<String> getListeFollowings() {
        return listeFollowings;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println("Entrez votre nom: ");
            name = in.readLine();
            System.out.println("New connection from " + name);
            out.println("Taper /help pour afficher les commandes disponibles.");
            server.broadcast(name, name + " a rejoint le chat.");
            String line;

            while ((line = in.readLine()) != null) {
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
                        if (!renamed) {
                            server.broadcast(name, name + " a changé son nom en " + newName);
                            System.out.println(name + " a changé son nom en " + newName);
                            name = newName;
                            out.println("Votre nom a été changé en " + newName);
                        }
                    } else {
                        out.println("Commande Invalide. Usage: /nick <new name>");
                    }
                } else if (line.startsWith("/quit")) {
                    server.broadcast(name, name + " a quitté le chat.");
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
                        ConnectionHandler handlerQuiSuit = null;
                        if (followName.equals(name)) {
                            out.println("Vous ne pouvez pas vous suivre vous-même.");
                            continue;
                        }
                        for (ConnectionHandler handler : connections) {
                            if(handler.getName().equals(name)){
                                handlerQuiSuit = handler;
                            }
                            if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                                found = true;
                                if(handler.getListeFollowers().contains(followName)){
                                    out.println("Vous suivez déjà " + followName);
                                }

                                else{
                                    out.println("Vous suivez maintenant " + followName);
                                    handler.ajouterFollower(followName);
                                    handlerQuiSuit.ajouterFollowing(followName);
                                    handler.sendMessage(name + " vous suit maintenant.");
                                }
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
                        ConnectionHandler handlerQuiSuit = null;
                        for (ConnectionHandler handler : connections) {
                            if(handler.getName().equals(name)){
                                handlerQuiSuit = handler;
                            }
                            if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                                found = true;
                                if(!handler.getListeFollowers().contains(followName)){
                                    out.println("Vous ne suivez pas " + followName);
                                }
                                else{
                                    handlerQuiSuit.retirerFollowing(followName);
                                    handler.getListeFollowers().remove(followName);
                                    out.println("Vous ne suivez plus " + followName);
                                    handler.sendMessage(name + " ne vous suit plus.");
                                }
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
                    out.println("--> /voirProfil : Vous permet de voir votre profil. (nombre de followers, nombre de followings)");
                    out.println("--> /quit : Quitte le chat.");
                } else if (line.startsWith("/voirLesPersonnesSuivi")) {

                } else if(line.startsWith("/voirProfil")) {
                     for (ConnectionHandler handler : connections) {
                            if (handler != null && handler.getName() != null && handler.getName().equals(name)) {
                                out.println("Vous êtes suivi par " + handler.getNombreFollowers() + " personnes.");
                                out.println("Vous suivez " + handler.getNombreFollowings() + " personnes.");
                            }
                        }
                }else if(line.startsWith("/voirLeProfilAutreUtilisateur")){
                    String[] messageSplit = line.split(" ", 2);
                    if (messageSplit.length == 2) {
                        String followName = messageSplit[1];
                        boolean found = false;
                        for (ConnectionHandler handler : connections) {
                            if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                                found = true;
                                out.println(followName + " est suivi par " + handler.getNombreFollowers() + " personnes.");
                                out.println(followName + " suit " + handler.getNombreFollowings() + " personnes.");
                            }
                        }
                        if (!found) {
                            out.println("Utilisateur non trouvé.");
                        }
                    } else {
                        out.println("Commande Invalide. Usage: /voirProfilAutreUtilisateur <name>");
                    }
                }
                
                else {
                    server.broadcast(name, name + ": " + line);
                }

            }
        } catch (IOException e) {
            // Ici, on gère la déconnexion inattendue
            server.broadcast(name, name + " a quitté le chat de manière inattendue.");
        } finally {
            shutdown();
        }
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void shutdown() {
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
