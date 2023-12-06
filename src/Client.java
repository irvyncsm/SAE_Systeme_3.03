import java.io.*;
import java.net.Socket;

public class Client implements Runnable{
    
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {
        try {
            client = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            done = false;

            InputHandler inputHandler = new InputHandler();
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
        done = true;
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

    class InputHandler implements Runnable{

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = input.readLine();
                    if (message.equals("exit")) {
                        input.close();
                        shutdown();
                    } else {
                        out.println(message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }   
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
