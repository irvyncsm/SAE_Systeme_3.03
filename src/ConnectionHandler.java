import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

/**
 * La classe ConnectionHandler gère la communication avec un client connecté à un serveur de chat.
 * Chaque instance de cette classe est associée à un client individuel.
 */

public class ConnectionHandler implements Runnable {
    private Socket client; // Le socket du client
    private BufferedReader in; // Flux d'entrée pour lire les données du client
    private PrintWriter out; // Flux de sortie pour envoyer des données au client
    private String name; // Nom de l'utilisateur associé à ce gestionnaire de connexion
    private Server server; // Référence au serveur auquel le gestionnaire de connexion est attaché
    private List<ConnectionHandler> connections; // Liste des autres gestionnaires de connexion du serveur
    private List<String> listeFollowers; // Liste des utilisateurs qui suivent l'utilisateur associé
    private List<String> listeFollowings; // Liste des utilisateurs suivis par l'utilisateur associé


    /**
     * Constructeur de la classe ConnectionHandler.
     * @param clientSocket Le socket du client.
     * @param server Référence au serveur auquel le gestionnaire de connexion est attaché.
     */
    public ConnectionHandler(Socket clientSocket, Server server) {
        this.connections = server.getConnections();
        this.client = clientSocket;
        this.server = server;
        this.listeFollowers = new ArrayList<>();
        this.listeFollowings = new ArrayList<>();
    }

    /**
     * Retourne le nombre d'utilisateurs qui suivent l'utilisateur associé.
     * @return Le nombre de followers.
     */
    public int getNombreFollowers() {
        return listeFollowers.size();
    }

    /**
     * Retourne le nombre d'utilisateurs suivis par l'utilisateur associé.
     * @return Le nombre de followings.
     */
    public int getNombreFollowings() {
        return listeFollowings.size();
    }

    /**
     * Permet à l'utilisateur associé de suivre un autre utilisateur.
     * @param client Le nom de l'utilisateur à suivre.
     */
    public void suivreClient(String client){
        listeFollowings.add(client);
    }

    /**
     * Ajoute un follower à la liste des utilisateurs qui suivent l'utilisateur associé.
     * @param client Le nom du follower à ajouter.
     */
    public void ajouterFollower(String client){
        listeFollowers.add(client);
    }

    /**
     * Ajoute un utilisateur à la liste des utilisateurs suivis par l'utilisateur associé.
     * @param client Le nom de l'utilisateur à suivre.
     */
    public void ajouterFollowing(String client){
        listeFollowings.add(client);
    }

    /**
     * Retire un follower de la liste des utilisateurs qui suivent l'utilisateur associé.
     * @param client Le nom du follower à retirer.
     */
    public void retirerFollower(String client){
        listeFollowers.remove(client);
    }

    /**
     * Retire un utilisateur de la liste des utilisateurs suivis par l'utilisateur associé.
     * @param client Le nom de l'utilisateur à ne plus suivre.
     */
    public void retirerFollowing(String client){
        listeFollowings.remove(client);
    }

    /**
     * Retourne la liste des followers de l'utilisateur associé.
     * @return La liste des followers.
     */
    public List<String> getListeFollowers() {
        return listeFollowers;
    }

    /**
     * Retourne la liste des utilisateurs suivis par l'utilisateur associé.
     * @return La liste des followings.
     */
    public List<String> getListeFollowings() {
        return listeFollowings;
    }

    /**
     * Méthode exécutée lorsqu'un thread est démarré. Gère la communication avec le client.
     */
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
            server.broadcast(name, name + " est connecté.");
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
                            server.broadcast(name, name + " a changé son nom en " + newName);
                            System.out.println(name + " a changé son nom en " + newName);
                            name = newName;
                            out.println("Votre nom a été changé en " + newName);
                        }
                    } else {
                        out.println("Commande Invalide. Usage: /nick <new name>");
                    }
                } else if (line.startsWith("/quit")) {
                    server.broadcast(name, name + " vient de se déconnecter.");
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
                }else if (line.startsWith("/unfollow")) {
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
                    out.println("--> /profil : Vous permet de voir votre profil. (nombre de followers, nombre de followings)");
                    out.println("--> /voirProfil <name> : Vous permet de voir le profil d'un utilisateur. (nombre de followers, nombre de followings)");
                    out.println("--> /quit : Quitte le chat.");
                } else if(line.startsWith("/profil")) {
                     for (ConnectionHandler handler : connections) {
                            if (handler != null && handler.getName() != null && handler.getName().equals(name)) {
                                out.println("Vous êtes suivi par " + handler.getNombreFollowers() + " personnes.");
                                out.println("Vous suivez " + handler.getNombreFollowings() + " personnes.");
                            }
                        }
                }else if(line.startsWith("/voirProfil")){
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
                        out.println("Commande Invalide. Usage: /voirProfil <name>");
                    }
                }else {
                    // Si le message n'est pas une commande, on l'envoie à tous les clients connectés
                    //
                    //
                    //
                    server.broadcast(name, name + ": " + line);
                }
            }
        } catch (IOException e) {
            // Ici, on gère la déconnexion inattendue
            server.broadcast(name, name + " s'est déconnecté de manière inattendue.");
        } finally {
            shutdown();
        }
    }

    /**
     * Retourne le nom de l'utilisateur associé à ce gestionnaire de connexion.
     * @return Le nom de l'utilisateur.
     */
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
