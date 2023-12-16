class InputHandler
!!!129154.java!!!	InputHandler(inout client : Client)
            this.client = client;
            this.in = new BufferedReader(new InputStreamReader(System.in));
            this.out = new PrintWriter(client.getClient().getOutputStream(), true);
!!!129282.java!!!	run() : void
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
