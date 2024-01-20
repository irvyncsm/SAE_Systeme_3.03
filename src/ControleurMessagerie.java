import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurMessagerie implements EventHandler<ActionEvent>{

    private ChatApplication appli;
    private Client client;

    public ControleurMessagerie(ChatApplication appli, Client client) {
        this.appli = appli;
        this.client = client;
    }

    @Override
    public void handle(ActionEvent event) {
            String message = this.appli.getMessage();
            String username = "";
            try {
                username = this.appli.getClient().getUser().getUsername();
            } catch (Exception e) {
            }
            Message m = new Message(0, username, message, new Date(), 0);
            String json = m.toJSON();
            if (!message.isEmpty()) {
                System.out.println("Message envoyé : " + json);
                sendMessage(json);
            }
            
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            client.handleUserMessage(message);
            this.appli.getMessageField().clear();
        }
    }
}