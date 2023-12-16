class Client
!!!128002.java!!!	run() : void
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
!!!128130.java!!!	shutdown() : void
        try{
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
!!!128258.java!!!	getClient() : Socket
        return client;
!!!128386.java!!!	main(inout args : String [[]]) : void
        Client client = new Client();
        client.run();
