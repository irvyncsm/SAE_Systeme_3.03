import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private int id;
    private String user;
    private String content;
    private Date date;
    private int likes;

    public Message(int id, String user, String content, Date date, int likes) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String toJSON() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = dateFormat.format(this.date);

        return String.format("{\"id\": %d, \"user\": \"%s\", \"content\": \"%s\", \"date\": \"%s\", \"likes\": %d}",
                this.id, this.user, this.content, formattedDate, this.likes);
    }

    public static void main(String[] args) throws ParseException {
        Message message = new Message(987697, "toto", "Hello world !", new Date(), 3);
        System.out.println(message.toJSON());
    }
}
