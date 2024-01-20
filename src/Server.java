import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
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
                ConnectionHandler handler = new ConnectionHandler(client, this);
                synchronized (connections) {
                    connections.add(handler);
                }
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String senderName, String message) {
        synchronized (connections) {
            for (ConnectionHandler handler : connections) {
                if (handler != null && handler.getName() != null && !handler.getName().equals(senderName)) {
                    handler.sendMessage(message);
                    System.out.println(senderName + " à envoyé : " + message + " à " + handler.getName());
                }
            }
        }
    }
    
    public void postMessage(String senderName, String message, List<String> listeFollowers) {
        synchronized (connections) {
            for (ConnectionHandler handler : connections) {
                if (handler != null && handler.getName() != null && !handler.getName().equals(senderName)) {
                    if (listeFollowers.contains(handler.getName())) {
                        handler.sendMessage(message);
                        System.out.println(senderName + " à envoyé : " + message + " à " + handler.getName());
                    }
                }
            }
        }
    }
    
    public void shutdown() {
        try {
            done = true;
            if (!server.isClosed()) {
                server.close();
            }
            synchronized (connections) {
                for (ConnectionHandler handler : connections) {
                    handler.shutdown();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeConnection(ConnectionHandler handler) {
        synchronized (connections) {
            connections.remove(handler);
        }
    }

    public ArrayList<ConnectionHandler> getConnections() {
        return connections;
    }
    

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}