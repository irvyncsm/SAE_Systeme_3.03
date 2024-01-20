import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * La classe ChatApplication est l'entrée principale de l'application de chat JavaFX.
 * Elle étend la classe Application et gère l'initialisation de l'interface graphique,
 * la création du client de chat, et les interactions avec l'utilisateur.
 */
public class ChatApplication extends Application {

    private TextArea chatArea; // Zone d'affichage des messages
    private TextField messageTextField; // Champ de saisie de messages
    private Button BtnEnvoyerMessage; // Bouton pour envoyer les messages
    private Label statutLabel; // Étiquette affichant le statut de la connexion

    private Client client; // Instance du client de chat

    /**
     * Point d'entrée principal de l'application JavaFX.
     * @param args Les arguments de ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Méthode de démarrage de l'application JavaFX.
     * @param stage La scène principale de l'application.
     */
    @Override
    public void start(Stage stage) {
        // Initialisation du client de chat
        client = new Client(this);

        // Initialisation des composants graphiques
        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageTextField = new TextField();
        messageTextField.setOnKeyReleased(new ControleurEnterMessagerie(this, client));
        BtnEnvoyerMessage = new Button("Envoyer");
        BtnEnvoyerMessage.setOnAction(new ControleurMessagerie(this, client));

        statutLabel = new Label("Impossible de se connecter au serveur. Nouvelle tentative dans 5 secondes.");

        // Création de la vue de la messagerie
        VueMessagerie vueMessagerie = new VueMessagerie(chatArea, messageTextField, BtnEnvoyerMessage, statutLabel);
        BorderPane pageAccueil = vueMessagerie;

        // Configuration de la scène principale
        stage.setTitle("Instagram");
        stage.setScene(new Scene(pageAccueil, 600, 400));

        // Gestion de la fermeture de l'application
        stage.setOnCloseRequest(e -> {
            if (client != null) {
                client.shutdown();
            }
            Platform.exit();
        });

        // Affichage de la scène principale
        stage.show();
    }

    /**
     * Récupère le texte du champ de saisie de messages.
     * @return Le texte du champ de saisie.
     */
    public String getMessage() {
        return messageTextField.getText().trim();
    }

    /**
     * Récupère le champ de saisie de messages.
     * @return Le champ de saisie de messages.
     */
    public TextField getMessageField() {
        return messageTextField;
    }

    /**
     * Efface la zone d'affichage des messages.
     */
    public void clearChatArea() {
        Platform.runLater(() -> chatArea.clear());
    }

    /**
     * Désactive ou active le champ de saisie de messages.
     * @param disabled Si vrai, le champ de saisie est désactivé ; sinon, il est activé.
     */
    public void setTextFieldDisabled(boolean disabled) {
        Platform.runLater(() -> messageTextField.setDisable(disabled));
    }

    /**
     * Traite et affiche un message dans la zone d'affichage des messages.
     * @param message Le message à afficher.
     */
    public void traiterMessage(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    /**
     * Met à jour le statut de la connexion affiché dans l'interface graphique.
     * @param statut Le nouveau statut de la connexion.
     */
    public void setStatut(String statut) {
        Platform.runLater(() -> statutLabel.setText(statut));

        // Change la couleur du texte en fonction du statut
        if (statut.equals("Connecté au serveur.")) {
            Platform.runLater(() -> statutLabel.setStyle("-fx-text-fill: green;"));
        } else {
            Platform.runLater(() -> statutLabel.setStyle("-fx-text-fill: red;"));
        }
    }

    /**
     * Récupère l'instance du client de chat.
     * @return L'instance du client de chat.
     */
    public Client getClient() {
        return client;
    }
}
