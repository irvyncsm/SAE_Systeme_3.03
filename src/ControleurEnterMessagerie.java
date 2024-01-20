import java.util.Date;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Contrôleur pour gérer les événements liés à l'envoi de messages lorsqu'une touche est enfoncée.
 */
public class ControleurEnterMessagerie implements EventHandler<KeyEvent> {

    private ChatApplication appli;
    private Client client;

    /**
     * Initialise un nouveau ContrôleurEnterMessagerie.
     *
     * @param appli  L'application de chat associée.
     * @param client Le client associé.
     */
    public ControleurEnterMessagerie(ChatApplication appli, Client client) {
        this.appli = appli;
        this.client = client;
    }

    /**
     * Gère l'événement lorsqu'une touche est enfoncée.
     *
     * @param event L'événement KeyEvent.
     */
    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
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
