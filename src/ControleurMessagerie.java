import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * La classe ControleurMessagerie agit en tant que contrôleur pour la messagerie d'une application JavaFX.
 * Elle implémente l'interface EventHandler pour gérer les actions liées à l'envoi de messages.
 */
public class ControleurMessagerie implements EventHandler<ActionEvent>{

    private ChatApplication appli; // Instance de l'application de chat
    private Client client; // Instance du client de chat

    /**
     * Constructeur de la classe ControleurMessagerie.
     * @param appli L'instance de l'application de chat.
     * @param client L'instance du client de chat.
     */
    public ControleurMessagerie(ChatApplication appli, Client client) {
        this.appli = appli;
        this.client = client;
    }

    /**
     * Gère l'événement lié à l'envoi de messages.
     * Récupère le message à partir de l'application, crée un objet Message, le convertit en format JSON,
     * et envoie le message via le client.
     * Efface ensuite le champ de saisie de message dans l'interface graphique.
     * @param event L'événement d'action déclenché lors de l'envoi de messages.
     */
    @Override
    public void handle(ActionEvent event) {
        String message = this.appli.getMessage();
        String username = "";

        try {
            // Récupère le nom d'utilisateur du client connecté
            username = this.appli.getClient().getUser().getUsername();
        } catch (Exception e) {
            System.out.println("Utilisateur null");
        }

        // Crée un objet Message avec les informations fournies
        Message m = new Message(0, username, message, new Date(), 0);
        String json = m.toJSON();

        if (!message.isEmpty()) {
            // Affiche le message dans la console
            System.out.println("Message envoyé : " + json);
            
            // Envoie le message via le client
            sendMessage(json);
        }   
    }

    /**
     * Envoie le message au serveur via le client.
     * Efface ensuite le champ de saisie de message dans l'interface graphique.
     * @param message Le message à envoyer.
     */
    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            // Utilise le client pour envoyer le message au serveur
            client.handleUserMessage(message);
            
            // Efface le champ de saisie de message dans l'interface graphique
            this.appli.getMessageField().clear();
        }
    }
}
