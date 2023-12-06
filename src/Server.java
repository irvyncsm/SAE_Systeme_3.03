import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;


    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String senderName, String message) {
        for (ConnectionHandler handler : connections) {
            if (handler != null && !handler.getName().equals(senderName)) {
                handler.sendMessage(message);
                System.out.println(senderName + " sent a message to " + handler.getName() + ": " + message);
            }
        }
    }
    

    public void shutdown(){
        try {
            done = true;
            if(!server.isClosed()) {
                server.close();
            }
            for(ConnectionHandler handler : connections){
                handler.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ConnectionHandler implements Runnable{
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String name;

        public ConnectionHandler(Socket clientSocket){
            this.client = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Please enter your name: ");
                name = in.readLine();
                System.out.println("New connection from: " + name);
                broadcast(name, name + " has joined the chat");
                String line;

                // Continuous loop to listen for messages
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("/nick ")) {
                        String[] messageSplit = line.split(" ", 2);
                        if (messageSplit.length == 2) {
                            String newName = messageSplit[1];
                            broadcast(name, name + " changed their name to " + newName);
                            System.out.println(name + " changed their name to " + newName);
                            name = newName;  // Update the 'name' field
                            out.println("Your name has been changed to " + name);
                        } else {
                            out.println("Invalid command. Usage: /nick <new name>");
                        }
                    } else if (line.startsWith("/quit")) {
                        broadcast(name, name + " has left the chat.");
                        break;  // Exit the loop and terminate the thread
                    } else {
                        broadcast(name, name + ": " + line);
                    }
                }
            } catch (IOException e) {
                shutdown();
            } finally {
                shutdown();  // Ensure cleanup even if an exception occurs
            }
        }



        public String getName() {
            return name;
        }

        public void sendMessage(String message){
            out.println(message);
        }

        public void shutdown(){
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}