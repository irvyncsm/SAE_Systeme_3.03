import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<User> followers;
    private List<User> following;
    private List<Message> messages;
    private List<Message> likes;

    public User(String username) {
        this.username = username;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.likes = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getLikes() {
        return likes;
    }

}
