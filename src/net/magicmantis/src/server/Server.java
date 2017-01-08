package net.magicmantis.src.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Joseph on 5/4/2015.
 */

public class Server {

    private ServerSocket serverSock; //socket to accept new connections
    private Socket sock; //reference to the socket established to the last connected client

    private ArrayList<User> users; //list of threads handling interaction with a particular user session
    private ArrayList<OnlineGame> games; //list of games currently hosted by the server

    int id; //incrementing id value assigned to new user connections

    /**
     * Create a new server instance that will bind to port 8080 and receive new incoming connections.
     * The server will also host games and their states.
     *
     * @throws IOException
     */
    public Server() throws IOException {
        System.out.println("Server Started.");

        //init connection variables
        id = 0;
        users = new ArrayList<User>();

        //init online game variables
        games = new ArrayList<OnlineGame>();
        Thread gameManager = new Thread() {
            public void run() {
                boolean running = true;
                while (running) {
                    try {
                        //end all stopped games
                        for (int i = 0; i < games.size(); i++) {
                            OnlineGame game = games.get(i);
                            if (game.isStarted() && game.getPlayers().isEmpty()) {
                                games.remove(game);
                                i--;
                            }
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        gameManager.start();

        //start server and listen for connections
        try {
            serverSock = new ServerSocket(8080);
        } catch (BindException be) {
            System.out.println("The socket is already in use.");
            System.exit(0);
        }
        System.out.println("Awaiting connectons...");
        while ((sock = serverSock.accept()) != null) {
            System.out.print("User connected: ");
            User joinedUser = new User(id, sock, this);
            id++;
            users.add(joinedUser);
            new Thread(joinedUser).start();
        }
    }

    /**
     * Entry point for the server.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        Server server = new Server();
    }

    public ArrayList<OnlineGame> getGamesList() {
        return games;
    }

}
