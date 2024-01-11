import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable{
    
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private InputHandler inputHandler;
    

    @Override
    public void run() {
        try {
            client = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            inputHandler = new InputHandler(this);
            Thread inputThread = new Thread(inputHandler);
            inputThread.start();

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown(){
        try{
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClient() {
        return client;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
