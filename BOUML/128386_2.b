class ConnectionHandler
!!!128514.java!!!	ConnectionHandler(inout clientSocket : Socket, inout server : Server)
            this.client = clientSocket;
            this.server = server;
            this.connections = server.getConnections();
!!!128642.java!!!	run() : void
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Entrez votre nom: ");
                name = in.readLine();
                System.out.println("New connection from " + name);
                out.println("Taper /help pour afficher les commandes disponibles.");
                server.broadcast(name, name + " a rejoint le chat.");
                String line;

                while ((line = in.readLine()) != null) {
                    if (line.startsWith("/nick")) {
                        String[] messageSplit = line.split(" ", 2);
                        if (messageSplit.length == 2) {
                            boolean renamed = false;
                            String newName = messageSplit[1];
                            if (newName.equals(name)) {
                                out.println("Votre nom est déjà " + newName);
                                continue;
                            } else if (newName.isEmpty()) {
                                out.println("Votre nom ne peut pas être vide.");
                                continue;
                            } else {
                                for (ConnectionHandler handler : connections) {
                                    if (handler != null && handler.getName() != null && handler.getName().equals(newName)) {
                                        out.println("Ce nom est déjà pris.");
                                        renamed = true;
                                        break;
                                    }
                                }
                            }
                            if (!renamed){
                                server.broadcast(name, name + " a changé son nom en " + newName);
                                System.out.println(name + " a changé son nom en " + newName);
                                name = newName;
                                out.println("Votre nom a été changé en " + newName);
                            }
                        } else {
                            out.println("Commande Invalide. Usage: /nick <new name>");
                        }
                    } else if (line.startsWith("/quit")) {
                        server.broadcast(name, name + " a quitté le chat.");
                        server.removeConnection(this);
                        break;
                    } else if (line.startsWith("/list")) {
                        out.println("Liste des utilisateurs connectés: ");
                        for (ConnectionHandler handler : connections) {
                            if (handler != null && handler.getName() != null) {
                                out.println(handler.getName());
                            }
                        }
                    } else if (line.startsWith("/follow")) {
                        String[] messageSplit = line.split(" ", 2);
                        if (messageSplit.length == 2) {
                            String followName = messageSplit[1];
                            boolean found = false;
                            if (followName.equals(name)) {
                                out.println("Vous ne pouvez pas vous suivre vous-même.");
                                continue;
                            }
                            for (ConnectionHandler handler : connections) {
                                if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                                    out.println("Vous suivez maintenant " + followName);
                                    found = true;
                                    handler.sendMessage(name + " vous suit maintenant.");
                                }
                            }
                            if (!found) {
                                out.println("Utilisateur non trouvé.");
                            }
                        } else {
                            out.println("Commande Invalide. Usage: /follow <name>");
                        }
                    } else if (line.startsWith("/unfollow")) {
                        String[] messageSplit = line.split(" ", 2);
                        if (messageSplit.length == 2) {
                            String followName = messageSplit[1];
                            boolean found = false;
                            for (ConnectionHandler handler : connections) {
                                if (handler != null && handler.getName() != null && handler.getName().equals(followName)) {
                                    out.println("Vous ne suivez plus " + followName);
                                    found = true;
                                    handler.sendMessage(name + " ne vous suit plus.");
                                }
                            }
                            if (!found) {
                                out.println("Utilisateur non trouvé.");
                            }
                        } else {
                            out.println("Commande Invalide. Usage: /unfollow <name>");
                        }
                    } else if (line.startsWith("/like") || line.equals("like ")) {
                        out.println("Fonctionnalité /like <id_message> non implémentée.");
                    } else if (line.startsWith("/unlike") || line.equals("unlike ")) {
                        out.println("Fonctionnalité /unlike <id_message> non implémentée.");
                    } else if (line.startsWith("/delete") || line.equals("delete ")) {
                        out.println("Fonctionnalité /delete <id_message> non implémentée.");
                    } else if (line.startsWith("/help") || line.equals("help ")) {
                        out.println("");
                        out.println("Commandes disponibles: ");
                        out.println("--> /nick <new name> : Change votre nom.");
                        out.println("--> /list : Liste les utilisateurs connectés.");
                        out.println("--> /follow <name> : Vous permet de suivre un utilisateur.");
                        out.println("--> /unfollow <name> : Vous permet de ne plus suivre un utilisateur.");
                        out.println("--> /like <id_message> : Vous permet de liker un message.");
                        out.println("--> /unlike <id_message> : Vous permet de unliker un message.");
                        out.println("--> /delete <id_message> : Vous permet de supprimer un message.");
                        out.println("--> /quit : Quitte le chat.");
                    } else {
                        server.broadcast(name, name + ": " + line);
                    }
                }
            } catch (IOException e) {
                // Ici, on gère la déconnexion inattendue
                server.broadcast(name, name + " a quitté le chat de manière inattendue.");
            } finally {
                shutdown();
            }
!!!128770.java!!!	getName() : String
            return name;
!!!128898.java!!!	sendMessage(in message : String) : void
            out.println(message);
!!!129026.java!!!	shutdown() : void
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
                synchronized (connections) {
                    connections.remove(this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
