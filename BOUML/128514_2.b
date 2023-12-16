class Server
!!!129410.java!!!	Server()
        connections = new ArrayList<>();
        done = false;
!!!129538.java!!!	run() : void
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
!!!129666.java!!!	broadcast(in senderName : String, in message : String) : void
        synchronized (connections) {
            for (ConnectionHandler handler : connections) {
                if (handler != null && handler.getName() != null && !handler.getName().equals(senderName)) {
                    handler.sendMessage(message);
                    System.out.println(senderName + " à envoyé : " + message + " à " + handler.getName());
                }
            }
        }
!!!129794.java!!!	shutdown() : void
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
!!!129922.java!!!	getConnections() : ConnectionHandler
        return connections;
!!!130050.java!!!	removeConnection(inout handler : ConnectionHandler) : void
        synchronized (connections) {
            connections.remove(handler);
        }
!!!130178.java!!!	main(inout args : String [[]]) : void
        Server server = new Server();
        server.run();
