import java.util.Date;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class ControleurEnterMessagerie implements EventHandler<KeyEvent>{
    
    private ChatApplication appli;
    private Client client;
    
    public ControleurEnterMessagerie(ChatApplication appli, Client client) {
        this.appli = appli;
        this.client = client;
    }
    
    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String message = this.appli.getMessage();
            String username = "";
            try {
                username = this.appli.getClient().getUser().getUsername();
            } catch (Exception e) {
                System.out.println("Utilisateur null");
            }
            Message m = new Message(0, username, message, new Date(), 0);
            String json = m.toJSON();
            if (!message.isEmpty()) {
                System.out.println("Message envoyé : " + json);
                sendMessage(json);
            }
        }
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            client.handleUserMessage(message);
            this.appli.getMessageField().clear();
        }
    }
}