package net.magicmantis.src.server;

import net.magicmantis.src.exceptions.AccessDeniedException;
import net.magicmantis.src.exceptions.FailedStartGameException;
import net.magicmantis.src.exceptions.GameNotFoundException;
import net.magicmantis.src.exceptions.UnknownOptionException;
import net.magicmantis.src.services.ServerController;
import net.magicmantis.src.services.ServerControllerServer;
import net.magicmantis.src.view.Game;
import net.magicmantis.src.services.TextEngine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * User: each instance of this class represents a thread handling a single user connection.
 */

public class User implements Runnable {

    private int id;
    private String username;
    private OnlineGame game;

    private Socket sock;
    private boolean disconnected;
    private DataInputStream in;
    private DataOutputStream out;
    private ServerController serverController;

    public HashMap<String, Boolean> userInput;

    public User(int id, Socket sock, Server server) throws IOException {
        disconnected = false;
        this.id = id;
        this.username = TextEngine.namebank[Game.rand.nextInt(TextEngine.namebank.length)];
        System.out.println(username + " - id: "+id);
        this.sock = sock;
        out = new DataOutputStream(sock.getOutputStream());
        in = new DataInputStream(sock.getInputStream());
        serverController = new ServerControllerServer(server.getGamesList(), this);

        initInput();
    }

    /**
     * Set up hash map instance to store input information from the user.
     */
    private void initInput() {
        userInput = new HashMap<String, Boolean>();
        userInput.put("left",false);
        userInput.put("right",false);
        userInput.put("up",false);
        userInput.put("down",false);
        userInput.put("space",false);
        userInput.put("d",false);
    }

    @Override
    public void run() {
        while (!disconnected) {
            try {
                try {
                    int code = in.readInt();

                    switch (code) {
                        case 0: disconnect(); break;
                        case 1: serverController.connect(); break;
                        case 2: serverController.authenticate(); break;
                        case 3: serverController.matchMake(); break;
                        case 4: serverController.getGameInfo(); break;
                        case 5: serverController.startGame(); break;
                        case 6: serverController.getLevel(); break;
                        case 7: serverController.updateInput(); break;
                        case 8: serverController.changeOption(null, null); break;
                        case 9: serverController.changeTeam(0, true); break;
                        default: disconnect(); break;
                    }
                }
                catch (GameNotFoundException | FailedStartGameException | ClassNotFoundException | UnknownOptionException e){
                    out.writeInt(-1);
                } catch (AccessDeniedException e) {
                    out.writeInt(-2);
                }
            } catch (IOException e) {
                System.out.println("User disconnected: "+username);
                disconnect();
            }
        }
    }

    private void disconnect() {
        this.disconnected = true;
        if (game != null) this.game.removeUser(this);
    }

    public DataInputStream getInput() {
        return in;
    }

    public DataOutputStream getOutput() {
        return out;
    }

    public OnlineGame getGame() {
        return game;
    }

    public int getID() {
        return id;
    }

    public String getUsername() { return username; }

    public void setGame(OnlineGame game) {
        this.game = game;
    }

}
