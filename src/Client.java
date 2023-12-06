import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private MessageHandler messageHandler;

    public Client(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        connectToServer();
    }

    private void connectToServer() {
        try {
            client = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void handleUserMessage(String message) {
        messageHandler.handleMessage("Vous: " + message);
        sendMessage(message);
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                messageHandler.handleMessage(message);
            }
        } catch (IOException e) {
            shutdown();
        }
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

    interface MessageHandler {
        void handleMessage(String message);
    }
}
