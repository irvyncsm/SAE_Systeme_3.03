import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;

/**
 * Représente la vue principale de la messagerie dans l'application, comprenant une zone de chat, un champ de saisie de message,
 * un bouton pour envoyer les messages, et une étiquette d'état.
 */
public class VueMessagerie extends BorderPane {
    private TextArea chatArea;
    private TextField messageTextField;
    private Button BtnEnvoyerMessage;
    private Label statutLabel;

    /**
     * Initialise une nouvelle instance de la vue de messagerie avec les composants nécessaires.
     *
     * @param chatArea           La zone de texte pour afficher les messages.
     * @param messageTextField   Le champ de texte pour la saisie des messages.
     * @param BtnEnvoyerMessage  Le bouton pour envoyer les messages.
     * @param statutLabel        L'étiquette d'état pour afficher des informations supplémentaires.
     */
    public VueMessagerie(TextArea chatArea, TextField messageTextField, Button BtnEnvoyerMessage, Label statutLabel) {
        super();
        this.chatArea = chatArea;
        this.messageTextField = messageTextField;
        this.BtnEnvoyerMessage = BtnEnvoyerMessage;
        this.statutLabel = statutLabel;
        this.setCenter(creerPage());
    }

    /**
     * Crée la structure de la page principale de la messagerie, avec la zone de chat au centre, le champ de saisie de message en bas,
     * et l'étiquette d'état en haut.
     *
     * @return La structure BorderPane de la page.
     */
    private BorderPane creerPage() {
        BorderPane racine = new BorderPane();
        racine.setPadding(new Insets(10));
        racine.setCenter(chatArea);
        racine.setBottom(creerSaisieMessage());
        racine.setTop(statutLabel);
        return racine;
    }

    /**
     * Crée la structure du bas de la page, avec le champ de saisie de message au centre et le bouton d'envoi à droite.
     *
     * @return La structure BorderPane de la partie basse de la page.
     */
    private BorderPane creerSaisieMessage() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(messageTextField);
        borderPane.setRight(BtnEnvoyerMessage);
        BorderPane.setMargin(messageTextField, new Insets(0, 0, 0, 5));
        return borderPane;
    }
}
