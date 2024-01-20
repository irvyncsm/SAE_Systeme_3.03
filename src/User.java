import java.util.ArrayList;
import java.util.List;

/**
 * Représente un utilisateur dans l'application de chat avec des informations associées telles que les followers,
 * les personnes suivies, les messages et les likes.
 */
public class User {
    private String username;
    private List<User> followers;
    private List<User> following;
    private List<Message> messages;
    private List<Message> likes;

    /**
     * Initialise une nouvelle instance de l'utilisateur avec le nom d'utilisateur spécifié.
     *
     * @param username Le nom d'utilisateur de l'utilisateur.
     */
    public User(String username) {
        this.username = username;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.likes = new ArrayList<>();
    }

    /**
     * Obtient le nom d'utilisateur de l'utilisateur.
     *
     * @return Le nom d'utilisateur.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Définit le nom d'utilisateur de l'utilisateur.
     *
     * @param username Le nom d'utilisateur à définir.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtient la liste des followers de l'utilisateur.
     *
     * @return La liste des followers.
     */
    public List<User> getFollowers() {
        return followers;
    }

    /**
     * Obtient la liste des utilisateurs suivis par l'utilisateur actuel.
     *
     * @return La liste des utilisateurs suivis.
     */
    public List<User> getFollowing() {
        return following;
    }

    /**
     * Obtient la liste des messages publiés par l'utilisateur.
     *
     * @return La liste des messages de l'utilisateur.
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Obtient la liste des messages likés par l'utilisateur.
     *
     * @return La liste des messages likés.
     */
    public List<Message> getLikes() {
        return likes;
    }
}
