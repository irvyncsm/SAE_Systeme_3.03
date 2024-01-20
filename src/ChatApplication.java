// cd Documents\GitHub\SAE_Systeme_3.03\src
// javac -d ../bin/ --module-path c:/Java/javafx-sdk-20.0.1/lib/ --add-modules javafx.controls *.java
// java -cp ../bin/ --module-path c:/Java/javafx-sdk-20.0.1/lib/ --add-modules javafx.controls ChatApplication

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Une application de chat simple utilisant JavaFX.
 */
public class ChatApplication extends Application {

    private TextArea chatArea;
    private TextField messageTextField;
    private Button BtnEnvoyerMessage;
    private Label statutLabel;

    private Client client;

    /**
     * Le point d'entrée de l'application.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initialise et configure l'interface utilisateur de l'application.
     *
     * @param stage La fenêtre principale de l'application.
     */
    @Override
    public void start(Stage stage) {
        // Initialisation du client
        client = new Client(this);

        // Configuration de l'interface utilisateur
        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageTextField = new TextField();
        messageTextField.setOnKeyReleased(new ControleurEnterMessagerie(this, client));
        BtnEnvoyerMessage = new Button("Envoyer");
        BtnEnvoyerMessage.setOnAction(new ControleurMessagerie(this, client));

        statutLabel = new Label("Impossible de se connecter au serveur. Nouvelle tentative dans 5 secondes.");

        VueMessagerie vueMessagerie = new VueMessagerie(chatArea, messageTextField, BtnEnvoyerMessage, statutLabel);
        BorderPane pageAcueil = vueMessagerie;

        // Configuration de la scène
        stage.setTitle("Instagram");
        stage.setScene(new Scene(pageAcueil, 600, 400));
        stage.setOnCloseRequest(e -> {
            // Fermeture propre du client et de l'application en cas de fermeture de la fenêtre
            if (client != null) {
                client.shutdown();
            }
            Platform.exit();
        });
        stage.show();
    }

    /**
     * Obtient le message entré par l'utilisateur.
     *
     * @return Le message entré.
     */
    public String getMessage() {
        return messageTextField.getText().trim();
    }

    /**
     * Obtient le champ de texte du message.
     *
     * @return Le champ de texte du message.
     */
    public TextField getMessageField() {
        return messageTextField;
    }

    /**
     * Efface la zone de chat.
     */
    public void clearChatArea() {
        Platform.runLater(() -> chatArea.clear());
    }

    /**
     * Désactive ou active le champ de texte du message.
     *
     * @param disabled true pour désactiver, false pour activer.
     */
    public void setTextFieldDisabled(boolean disabled) {
        Platform.runLater(() -> messageTextField.setDisable(disabled));
    }

    /**
     * Traite un message et l'affiche dans la zone de chat.
     *
     * @param message Le message à traiter et afficher.
     */
    public void traiterMessage(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    /**
     * Définit le statut de la connexion et met à jour l'affichage en conséquence.
     *
     * @param statut Le statut de la connexion.
     */
    public void setStatut(String statut) {
        Platform.runLater(() -> statutLabel.setText(statut));
        if (statut.equals("Connecté au serveur.")) {
            Platform.runLater(() -> statutLabel.setStyle("-fx-text-fill: green;"));
        } else {
            Platform.runLater(() -> statutLabel.setStyle("-fx-text-fill: red;"));
        }
    }

    /**
     * Obtient l'instance du client associée à l'application.
     *
     * @return L'instance du client.
     */
    public Client getClient() {
        return client;
    }
}

