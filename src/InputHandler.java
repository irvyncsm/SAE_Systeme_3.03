import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class InputHandler implements Runnable{

        private Client client;
        private PrintWriter out;
        private BufferedReader in;

        public InputHandler(Client client) throws IOException {
            this.client = client;
            this.in = new BufferedReader(new InputStreamReader(System.in));
            this.out = new PrintWriter(client.getClient().getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message.equals("exit")) {
                        in.close();
                        client.shutdown();
                    } else {
                        out.println(message);
                    }
                }
            } catch (IOException e) {
                client.shutdown();
            }   
        }
    }