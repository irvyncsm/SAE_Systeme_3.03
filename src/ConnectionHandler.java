import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;


/**
 * Gère la connexion avec un client sur le serveur de messagerie.
 */
public class ConnectionHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    private Server server;
    private List<ConnectionHandler> connections;
    private List<String> listeFollowers;
    private List<String> listeFollowings;

    /**
     * Initialise un nouvel objet ConnectionHandler.
     *
     * @param clientSocket Le socket du client.
     * @param server       Le serveur auquel le client est connecté.
     */
    public ConnectionHandler(Socket clientSocket, Server server) {
        this.connections = server.getConnections();
        this.client = clientSocket;
        this.server = server;
        this.listeFollowers = new ArrayList<>();
        this.listeFollowings = new ArrayList<>();
    }

    /**
     * Obtient le nombre de followers de ce client.
     *
     * @return Le nombre de followers.
     */
    public int getNombreFollowers() {
        return listeFollowers.size();
    }

    /**
     * Obtient le nombre de followings de ce client.
     *
     * @return Le nombre de followings.
     */
    public int getNombreFollowings() {
        return listeFollowings.size();
    }

    /**
     * Permet à ce client de suivre un autre client.
     *
     * @param client Le nom du client à suivre.
     */
    public void suivreClient(String client){
        listeFollowings.add(client);
    }

    /**
     * Ajoute un follower à la liste de followers de ce client.
     *
     * @param client Le nom du follower à ajouter.
     */
    public void ajouterFollower(String client){
        listeFollowers.add(client);
    }

    /**
     * Ajoute un following à la liste de followings de ce client.
     *
     * @param client Le nom du following à ajouter.
     */
    public void ajouterFollowing(String client){
        listeFollowings.add(client);
    }

    /**
     * Retire un follower de la liste de followers de ce client.
     *
     * @param client Le nom du follower à retirer.
     */
    public void retirerFollower(String client){
        listeFollowers.remove(client);
    }

    /**
     * Retire un following de la liste de followings de ce client.
     *
     * @param client Le nom du following à retirer.
     */
    public void retirerFollowing(String client){
        listeFollowings.remove(client);
    }

    /**
     * Obtient la liste des followers de ce client.
     *
     * @return La liste des followers.
     */
    public List<String> getListeFollowers() {
        return listeFollowers;
    }

    /**
     * Obtient la liste des followings de ce client.
     *
     * @return La liste des followings.
     */
    public List<String> getListeFollowings() {
        return listeFollowings;
    }

    /**
     * Exécute le thread du gestionnaire de connexion avec le client.
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            requestName(in);
            afficherAide();

            String readLine;

            while ((readLine = in.readLine()) != null) {
                System.out.println("Message reçu : " + readLine);
                String line = readLine.substring(readLine.indexOf("content") + 10, readLine.indexOf("date") - 3);
                line = line.substring(1, line.length() - 1);

                if (line.startsWith("/nick")) {
                    String[] messageSplit = line.split(" ", 2);
                    nick(messageSplit);
                } else if (line.startsWith("/quit")) {
                    server.broadcast(this.name, this.name + " vient de se déconnecter.");
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
                    follow(messageSplit);
                }else if (line.startsWith("/unfollow")) {
                    String[] messageSplit = line.split(" ", 2);
                    unfollow(messageSplit);             
                } else if (line.startsWith("/like") || line.equals("like ")) {
                    out.println("Fonctionnalité /like <id_message> non implémentée.");
                } else if (line.startsWith("/unlike") || line.equals("unlike ")) {
                    out.println("Fonctionnalité /unlike <id_message> non implémentée.");
                } else if (line.startsWith("/delete") || line.equals("delete ")) {
                    out.println("Fonctionnalité /delete <id_message> non implémentée.");
                } else if (line.startsWith("/help") || line.equals("help ")) {
                    afficherAide();
                } else if(line.startsWith("/profil")) {
                     afficherProfil();
                }else if(line.startsWith("/voirProfil")){
                    String[] messageSplit = line.split(" ", 2);
                    afficherAutreProfil(messageSplit);
                }else {
                    server.postMessage(this.name, this.name + ": " + line, listeFollowers);
                }
            }
        } catch (IOException e) {
            // Ici, on gère la déconnexion inattendue
            server.broadcast(this.name, this.name + " s'est déconnecté de manière inattendue.");
        } finally {
            shutdown();
        }
    }

    /**
     * Affiche le profil d'un autre client spécifié par son nom.
     *
     * @param messageSplit Le message entré par le client.
     */
    private void afficherAutreProfil(String[] messageSplit) {
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
    }

    /**
     * Affiche le profil de ce client, montrant les followers et followings.
     */
    private void afficherProfil() {
        for (ConnectionHandler handler : connections) {
            if (handler != null && handler.getName() != null && handler.getName().equals(this.name)) {
                out.println("Vous êtes suivi par " + handler.getNombreFollowers() + " personnes.");
                for (String follower : handler.getListeFollowers()) {
                    out.println(follower);
                }
                out.println("Vous suivez " + handler.getNombreFollowings() + " personnes.");
                for (String following : handler.getListeFollowings()) {
                    out.println(following);
                }
            }
        }
    }

    /**
     * Affiche les commandes disponibles pour le client.
     */
    private void afficherAide() {
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
    }

    /**
     * Méthode privée pour traiter la commande /unfollow.
     *
     * @param messageSplit Le message entré par le client.
     */
    private void unfollow(String[] messageSplit) {
        if (messageSplit.length == 2) {
            String unfollowName = messageSplit[1];
            boolean found = false;
            ConnectionHandler handlerCommand = null;
            for (ConnectionHandler handler : connections) {
                if(handler.getName().equals(this.name)){
                    handlerCommand = handler;
                }
                if (handler != null && handler.getName() != null && handler.getName().equals(unfollowName)) {
                    found = true;
                    if(!handlerCommand.getListeFollowings().contains(unfollowName)){
                        out.println("Vous ne suivez pas " + unfollowName);
                    }
                    else{
                        handlerCommand.retirerFollowing(unfollowName);
                        handler.getListeFollowers().remove(handlerCommand.getName());
                        out.println("Vous ne suivez plus " + unfollowName);
                        handler.sendMessage(this.name + " ne vous suit plus.");
                    }
                }
            }
            if (!found) {
                out.println("Utilisateur non trouvé.");
            }
        } else {
            out.println("Commande Invalide. Usage: /unfollow <name>");
        }   
    }

    /**
     * Méthode privée pour traiter la commande /follow.
     *
     * @param messageSplit Le message entré par le client.
     */
    private void follow(String[] messageSplit) {
        if (messageSplit.length == 2) {
            String followName = messageSplit[1];
            boolean found = false;
            ConnectionHandler handlerCommand = null;
            if (followName.equals(this.name)) {
                out.println("Vous ne pouvez pas vous suivre vous-même.");
                return;
            }
            for (ConnectionHandler handler : connections) {
                if(handler.getName().equals(this.name)){
                    handlerCommand = handler;
                }
                if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                    found = true;
                    if(handler.getListeFollowers().contains(followName)){
                        out.println("Vous suivez déjà " + followName);
                    }
                    else{
                        if (handlerCommand != null) {
                            handler.ajouterFollower(handlerCommand.getName());
                            handlerCommand.ajouterFollowing(followName);
                            out.println("Vous suivez maintenant " + followName);
                            handler.sendMessage(this.name + " vous suit maintenant.");
                        } else {
                            out.println("Erreur lors du traitement de la commande /follow");
                        }                                    
                    }
                }
            }
            if (!found) {
                out.println("Utilisateur non trouvé.");
            }
        } else {
            out.println("Commande Invalide. Usage: /follow <name>");
        }
    }

     /**
     * Méthode privée pour obtenir le nom du client à partir de sa réponse JSON.
     *
     * @param in Le BufferedReader pour lire l'entrée du client.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    private void requestName(BufferedReader in) throws IOException {
        out.println("Entrez votre nom: ");
        String nameData = in.readLine();
        // récupérer le nom de l'utilisateur dans la réponse JSON du client
        // ex: {"id": 0, "user": "", "content": "irvyn", "date": "2024-01-20T16:25:41Z", "likes": 0}
        this.name = nameData.substring(nameData.indexOf("content") + 10, nameData.indexOf("date") - 3);
        // enlever les guillemets autour du nom (ex: "toto" -> toto)
        this.name = this.name.substring(1, this.name.length() - 1);
        System.out.println("New connection from " + this.name);
        server.broadcast(this.name, this.name + " est connecté.");
    }

      /**
     * Méthode privée pour traiter la commande /nick.
     *
     * @param messageSplit Le message entré par le client.
     */
    private void nick(String[] messageSplit) {
        if (messageSplit.length == 2) {
            boolean renamed = false;
            String newName = messageSplit[1];
            if (newName.equals(this.name)) {
                out.println("Votre nom est déjà " + newName);
                return;
            } else if (newName.isEmpty()) {
                out.println("Votre nom ne peut pas être vide.");
                return;
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
                server.broadcast(this.name, this.name + " a changé son nom en " + newName);
                System.out.println(this.name + " a changé son nom en " + newName);
                this.name = newName;
                out.println("Votre nom a été changé en " + newName);
            }
        } else {
            out.println("Commande Invalide. Usage: /nick <new name>");
        }
    }

     /**
     * Obtient le nom de ce client.
     *
     * @return Le nom du client.
     */
    public String getName() {
        return this.name;
    }

     /**
     * Envoie un message à ce client.
     *
     * @param message Le message à envoyer.
     */
    public void sendMessage(String message){
        out.println(message);
    }

    /**
     * Ferme proprement la connexion avec ce client.
     */
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
