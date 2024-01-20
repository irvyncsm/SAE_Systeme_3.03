import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe Message représente un message dans le système de chat.
 * Chaque message a un identifiant unique, un utilisateur émetteur, un contenu textuel,
 * une date d'envoi, et un nombre de likes.
 */
public class Message {
    private int id; // Identifiant unique du message
    private String user; // Nom de l'utilisateur émetteur du message
    private String content; // Contenu textuel du message
    private Date date; // Date d'envoi du message
    private int likes; // Nombre de likes du message

    /**
     * Constructeur de la classe Message.
     * @param id Identifiant unique du message.
     * @param user Nom de l'utilisateur émetteur du message.
     * @param content Contenu textuel du message.
     * @param date Date d'envoi du message.
     * @param likes Nombre de likes du message.
     */
    public Message(int id, String user, String content, Date date, int likes) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.likes = likes;
    }

    /**
     * Retourne l'identifiant du message.
     * @return L'identifiant du message.
     */
    public int getId() {
        return id;
    }

    /**
     * Modifie l'identifiant du message.
     * @param id Le nouvel identifiant du message.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de l'utilisateur émetteur du message.
     * @return Le nom de l'utilisateur.
     */
    public String getUser() {
        return user;
    }

    /**
     * Modifie le nom de l'utilisateur émetteur du message.
     * @param user Le nouveau nom de l'utilisateur.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Retourne le contenu textuel du message.
     * @return Le contenu textuel du message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Modifie le contenu textuel du message.
     * @param content Le nouveau contenu textuel du message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retourne la date d'envoi du message.
     * @return La date d'envoi du message.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Modifie la date d'envoi du message.
     * @param date La nouvelle date d'envoi du message.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Retourne le nombre de likes du message.
     * @return Le nombre de likes du message.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Modifie le nombre de likes du message.
     * @param likes Le nouveau nombre de likes du message.
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Convertit le message en format JSON.
     * @return Une chaîne JSON représentant le message.
     */
    public String toJSON() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = dateFormat.format(this.date);

        return String.format("{\"id\": %d, \"user\": \"%s\", \"content\": \"%s\", \"date\": \"%s\", \"likes\": %d}",
                this.id, this.user, this.content, formattedDate, this.likes);
    }
}
