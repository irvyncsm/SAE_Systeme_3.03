import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ChatApplication application;
    private boolean connected;
    private User user;

    public Client(ChatApplication application) {
        this.application = application;
        connected = false;
        new Thread(this::connectToServer).start();
    }

    private void connectToServer() {
        while (!connected) {
            try {
                client = new Socket("localhost", 9999);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);

                new Thread(this::receiveMessages).start();
                System.out.println("Connecté au serveur.");
                this.application.setStatut("Connecté au serveur.");
                this.application.setTextFieldDisabled(false);
                connected = true;
            } catch (IOException e) {
                System.out.println("Impossible de se connecter au serveur. Nouvelle tentative dans 5 secondes.");
                this.application.setStatut("Impossible de se connecter au serveur. Nouvelle tentative dans 5 secondes.");
                this.application.clearChatArea();
                this.application.setTextFieldDisabled(true);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                application.traiterMessage(message);
            }
        } catch (IOException e) {
            shutdown();
            connected = false;
            new Thread(this::connectToServer).start();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void handleUserMessage(String message) {
        String content = message.substring(message.indexOf("content") + 10, message.indexOf("date") - 3);
        content = content.substring(1, content.length() - 1);
        application.traiterMessage("Vous: " + content);
        sendMessage(message);
    }

    public void shutdown() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    interface MessageHandler {
        void handleMessage(String message);
    }
}