import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Représente un message dans une application de chat.
 */
public class Message {
    private int id;
    private String user;
    private String content;
    private Date date;
    private int likes;

    /**
     * Initialise un nouvel objet Message.
     *
     * @param id      L'identifiant unique du message.
     * @param user    L'utilisateur qui a envoyé le message.
     * @param content Le contenu du message.
     * @param date    La date et l'heure auxquelles le message a été envoyé.
     * @param likes   Le nombre de likes reçus par le message.
     */
    public Message(int id, String user, String content, Date date, int likes) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.likes = likes;
    }

    /**
     * Obtient l'identifiant unique du message.
     *
     * @return L'ID du message.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique du message.
     *
     * @param id L'ID du message à définir.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient l'utilisateur qui a envoyé le message.
     *
     * @return Le nom d'utilisateur de l'expéditeur du message.
     */
    public String getUser() {
        return user;
    }

    /**
     * Définit l'utilisateur qui a envoyé le message.
     *
     * @param user Le nom d'utilisateur à définir.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Obtient le contenu du message.
     *
     * @return Le contenu du message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Définit le contenu du message.
     *
     * @param content Le contenu du message à définir.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Obtient la date et l'heure auxquelles le message a été envoyé.
     *
     * @return La date du message.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Définit la date et l'heure auxquelles le message a été envoyé.
     *
     * @param date La date du message à définir.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Obtient le nombre de likes reçus par le message.
     *
     * @return Le nombre de likes.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Définit le nombre de likes reçus par le message.
     *
     * @param likes Le nombre de likes à définir.
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Convertit l'objet message en une chaîne au format JSON.
     *
     * @return La représentation JSON du message.
     */
    public String toJSON() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = dateFormat.format(this.date);

        return String.format("{\"id\": %d, \"user\": \"%s\", \"content\": \"%s\", \"date\": \"%s\", \"likes\": %d}",
                this.id, this.user, this.content, formattedDate, this.likes);
    }
}
