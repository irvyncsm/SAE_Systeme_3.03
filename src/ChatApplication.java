// cd Documents\GitHub\SAE_Systeme_3.03\src
// javac -d ../bin/ --module-path c:/Java/javafx-sdk-20.0.1/lib/ --add-modules javafx.controls *.java
// java -cp ../bin/ --module-path c:/Java/javafx-sdk-20.0.1/lib/ --add-modules javafx.controls ChatApplication

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatApplication extends Application {

    private TextArea chatArea;
    private TextField messageTextField;
    private Button BtnEnvoyerMessage;
    private Label statutLabel;

    private Client client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        client = new Client(this);

        //

        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageTextField = new TextField();
        messageTextField.setOnKeyReleased(new ControleurEnterMessagerie(this, client));
        BtnEnvoyerMessage = new Button("Envoyer");
        BtnEnvoyerMessage.setOnAction(new ControleurMessagerie(this, client));

        statutLabel = new Label("Impossible de se connecter au serveur. Nouvelle tentative dans 5 secondes.");

        VueMessagerie vueMessagerie = new VueMessagerie(chatArea, messageTextField, BtnEnvoyerMessage, statutLabel);
        BorderPane pageAcueil = vueMessagerie;

        //

        stage.setTitle("Instagram");
        stage.setScene(new Scene(pageAcueil, 600, 400));
        stage.setOnCloseRequest(e -> {
            if (client != null) {
                client.shutdown();
            }
            Platform.exit();
        });
        stage.show();
    }

    public String getMessage() {
        return messageTextField.getText().trim();
    }

    public TextField getMessageField() {
        return messageTextField;
    }

    public void clearChatArea() {
        Platform.runLater(() -> chatArea.clear());
    }

    public void setTextFieldDisabled(boolean disabled) {
        Platform.runLater(() -> messageTextField.setDisable(disabled));
    }

    public void traiterMessage(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    public void setStatut(String statut) {
        Platform.runLater(() -> statutLabel.setText(statut));
        if (statut.equals("Connecté au serveur.")) {
            Platform.runLater(() -> statutLabel.setStyle("-fx-text-fill: green;"));
        } else {
            Platform.runLater(() -> statutLabel.setStyle("-fx-text-fill: red;"));
        }
    }
}

