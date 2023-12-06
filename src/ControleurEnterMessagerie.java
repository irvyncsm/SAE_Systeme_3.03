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
            if (!message.isEmpty()) {
                System.out.println("Message envoyé : " + message);
                sendMessage(message);
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