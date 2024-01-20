import java.util.Date;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * La classe ControleurEnterMessagerie agit en tant que contrôleur pour la messagerie d'une application JavaFX
 * en réagissant à l'événement de pression de la touche "Enter" dans la zone de saisie de messages.
 * Elle implémente l'interface EventHandler pour gérer les événements clavier.
 */
public class ControleurEnterMessagerie implements EventHandler<KeyEvent>{
    
    private ChatApplication appli; // Instance de l'application de chat
    private Client client; // Instance du client de chat
    
    /**
     * Constructeur de la classe ControleurEnterMessagerie.
     * @param appli L'instance de l'application de chat.
     * @param client L'instance du client de chat.
     */
    public ControleurEnterMessagerie(ChatApplication appli, Client client) {
        this.appli = appli;
        this.client = client;
    }
    
    /**
     * Gère l'événement lié à la pression de la touche "Enter" dans la zone de saisie de messages.
     * Récupère le message à partir de l'application, crée un objet Message, le convertit en format JSON,
     * et envoie le message via le client.
     * Efface ensuite le champ de saisie de message dans l'interface graphique.
     * @param event L'événement clavier déclenché lors de la pression de la touche "Enter".
     */
    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
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
