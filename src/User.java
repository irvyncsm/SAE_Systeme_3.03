import java.util.ArrayList;
import java.util.List;

/**
 * La classe User représente un utilisateur dans le système de chat.
 * Chaque utilisateur a un nom d'utilisateur unique et peut avoir des followers,
 * suivre d'autres utilisateurs, publier des messages, et aimer des messages d'autres utilisateurs.
 */
public class User {
    private String username; // Le nom d'utilisateur unique de l'utilisateur
    private List<User> followers; // Liste des utilisateurs qui suivent cet utilisateur
    private List<User> following; // Liste des utilisateurs suivis par cet utilisateur
    private List<Message> messages; // Liste des messages publiés par cet utilisateur
    private List<Message> likes; // Liste des messages aimés par cet utilisateur

    /**
     * Constructeur de la classe User.
     * @param username Le nom d'utilisateur unique de l'utilisateur.
     */
    public User(String username) {
        this.username = username;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.likes = new ArrayList<>();
    }

    /**
     * Retourne le nom d'utilisateur de l'utilisateur.
     * @return Le nom d'utilisateur.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Modifie le nom d'utilisateur de l'utilisateur.
     * @param username Le nouveau nom d'utilisateur.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retourne la liste des utilisateurs qui suivent cet utilisateur.
     * @return La liste des followers.
     */
    public List<User> getFollowers() {
        return followers;
    }

    /**
     * Retourne la liste des utilisateurs suivis par cet utilisateur.
     * @return La liste des following.
     */
    public List<User> getFollowing() {
        return following;
    }

    /**
     * Retourne la liste des messages publiés par cet utilisateur.
     * @return La liste des messages.
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Retourne la liste des messages aimés par cet utilisateur.
     * @return La liste des likes.
     */
    public List<Message> getLikes() {
        return likes;
    }
}
