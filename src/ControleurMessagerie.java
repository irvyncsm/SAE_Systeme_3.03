import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Contrôleur pour gérer les événements liés à l'envoi de messages lorsqu'un bouton est actionné.
 */
public class ControleurMessagerie implements EventHandler<ActionEvent> {

    private ChatApplication appli;
    private Client client;

    /**
     * Initialise un nouveau ContrôleurMessagerie.
     *
     * @param appli  L'application de chat associée.
     * @param client Le client associé.
     */
    public ControleurMessagerie(ChatApplication appli, Client client) {
        this.appli = appli;
        this.client = client;
    }

    /**
     * Gère l'événement lorsqu'un bouton est actionné.
     *
     * @param event L'événement ActionEvent.
     */
    @Override
    public void handle(ActionEvent event) {
        String message = this.appli.getMessage();
        String username = "";
        try {
            username = this.appli.getClient().getUser().getUsername();
        } catch (Exception e) {
            // Gestion de l'exception si le nom d'utilisateur n'est pas disponible
        }
        Message m = new Message(0, username, message, new Date(), 0);
        String json = m.toJSON();
        if (!message.isEmpty()) {
            System.out.println("Message envoyé : " + json);
            sendMessage(json);
        }
    }

    /**
     * Envoie le message au serveur via le client.
     *
     * @param message Le message à envoyer.
     */
    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            client.handleUserMessage(message);
            this.appli.getMessageField().clear();
        }
    }
}
