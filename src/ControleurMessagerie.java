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
            if (!message.isEmpty()) {
                System.out.println("Message envoyé : " + message);
                sendMessage(message);
            }
            
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            client.handleUserMessage(message);
            this.appli.getMessageField().clear();
        }
    }
}