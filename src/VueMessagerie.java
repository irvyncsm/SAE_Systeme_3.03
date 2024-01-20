import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;

/**
 * La classe VueMessagerie représente la vue graphique d'une messagerie dans une application JavaFX.
 * Elle comprend une zone de chat, une zone de saisie de message, un bouton d'envoi de message, et un label de statut.
 */
public class VueMessagerie extends BorderPane {
    private TextArea chatArea; // Zone de chat affichant les messages
    private TextField messageTextField; // Zone de saisie de messages
    private Button BtnEnvoyerMessage; // Bouton d'envoi de messages
    private Label statutLabel; // Label de statut

    /**
     * Constructeur de la classe VueMessagerie.
     * @param chatArea La zone de chat.
     * @param messageTextField La zone de saisie de message.
     * @param BtnEnvoyerMessage Le bouton d'envoi de message.
     * @param statutLabel Le label de statut.
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
     * Crée la mise en page de la vue de la messagerie avec la zone de chat en centre,
     * la zone de saisie de message en bas, et le label de statut en haut.
     * @return La mise en page BorderPane.
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
     * Crée la mise en page de la zone de saisie de message avec la zone de texte au centre
     * et le bouton d'envoi à droite.
     * @return La mise en page BorderPane.
     */
    private BorderPane creerSaisieMessage() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(messageTextField);
        borderPane.setRight(BtnEnvoyerMessage);
        BorderPane.setMargin(messageTextField, new Insets(0, 0, 0, 5));
        return borderPane;
    }
}
