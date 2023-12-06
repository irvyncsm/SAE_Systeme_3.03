import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;

public class VueMessagerie extends BorderPane {
    private TextArea chatArea;
    private TextField messageTextField;
    private Button BtnEnvoyerMessage;

    public VueMessagerie(TextArea chatArea, TextField messageTextField, Button BtnEnvoyerMessage) {
        super();
        this.chatArea = chatArea;
        this.messageTextField = messageTextField;
        this.BtnEnvoyerMessage = BtnEnvoyerMessage;
        this.setCenter(creerPage());
    }

    private BorderPane creerPage() {
        BorderPane racine = new BorderPane();
        racine.setPadding(new Insets(10));
        racine.setCenter(chatArea);
        racine.setBottom(creerSaisieMessage());
        return racine;
    }

    private BorderPane creerSaisieMessage() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(messageTextField);
        borderPane.setRight(BtnEnvoyerMessage);
        BorderPane.setMargin(messageTextField, new Insets(0, 0, 0, 5));
        return borderPane;
    }
}
