package net.magicmantis.src.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.magicmantis.src.exceptions.AccessDeniedException;
import net.magicmantis.src.exceptions.FailedStartGameException;
import net.magicmantis.src.exceptions.GameNotFoundException;
import net.magicmantis.src.exceptions.UnknownOptionException;
import net.magicmantis.src.model.Level;
import net.magicmantis.src.server.dataStructures.LevelData;
import net.magicmantis.src.server.OnlineGame;
import net.magicmantis.src.server.dataStructures.UserData;
import net.magicmantis.src.view.Game;
import net.magicmantis.src.view.HUD;

import java.awt.*;
import java.awt.image.DataBuffer;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ServerControllerProxy class: this class allows the client to access server functions necessary for
 * networked games.
 */

public class ServerControllerProxy implements ServerController {

    private boolean locked;
    private Game game;

    private Socket sock;
    private DataInputStream in;
    private DataOutputStream out;

    public ServerControllerProxy(Game game) throws IOException {
        this.game = game;
        this.locked = true;
        sock = new Socket("localhost", 8080);
        in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
        connect();
    }

    @Override
    public void disconnect() throws IOException {
        out.writeInt(0);
    }

    @Override
    public void connect() throws IOException {
        System.out.println("connect");
        out.writeInt(1);
        game.setSessionID(in.readInt());
        System.out.println("game "+game.getSessionID());
    }

    @Override
    public void authenticate() throws IOException {
        out.writeInt(1);
    }

    @Override
    public void matchMake() throws IOException, GameNotFoundException {
        System.err.println("matchMake()");
        out.writeInt(3);
        int gameID = in.readInt();
        if (gameID == -1) throw new GameNotFoundException();
        game.setGameID(gameID);
    }

    @Override
    public OnlineGame getGameInfo() throws IOException, GameNotFoundException {
        //send game info request to server
        System.out.println("getGameInfo()");
        out.writeInt(4);
        out.writeInt(game.getGameID());

        //receive response
        int gameID = in.readInt();
        if (gameID != game.getGameID()) throw new GameNotFoundException();

        int maxPlayers = in.readInt();
        int playerCount = in.readInt();

        //userdata
        String userDataString = in.readUTF();
        Type mapType = new TypeToken<Map<Integer, UserData>>(){}.getType();
        HashMap<Integer,UserData> userData = new Gson().fromJson(userDataString, mapType);

        //options
        String optionsString = in.readUTF();
        mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String,Object> options = new Gson().fromJson(optionsString, mapType);

        boolean started = in.readBoolean();

        OnlineGame info = new OnlineGame(gameID, maxPlayers);
        info.setPlayerCount(playerCount);
        info.setUserData(userData);
        info.setOptions(options);

        //start the game without starting the OnlineGame loop (since local copy is only used for information)
        if (started) info.startNoLoop();
        return info;
    }

    @Override
    public void changeOption(String option, Object value) throws IOException, UnknownOptionException, AccessDeniedException {
        System.err.println("changeOption()");
        //send changeOption request to server
        out.writeInt(8);
        out.writeInt(game.getGameID());

        //send option parameters
        out.writeUTF(option);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(value);

        //receive response status
        int status = in.readInt();

        if (status == -1) throw new UnknownOptionException();
        if (status == -2) throw new AccessDeniedException();
    }

    @Override
    public void changeTeam(int playerID, boolean up) throws IOException, AccessDeniedException {
        System.err.println("changeTeam()");
        //send changeTeam request to the server
        out.writeInt(9);
        out.writeInt(game.getGameID());

        //send player and direction
        out.writeInt(playerID);
        out.writeBoolean(up);

        //recieve response status
        int status = in.readInt();
    }

    @Override
    public void startGame() throws IOException, FailedStartGameException {
        System.err.println("startGame()");
        //send startGame request to server
        out.writeInt(5);
        out.writeInt(game.getGameID());

        //receive response status and fail if received bad response
        int status = in.readInt();
        if (status == -1) throw new FailedStartGameException();
    }

    @Override
    public void getLevel() throws IOException {
        synchronized (this) {
            System.err.println("getLevel()");
            //send get level request
            out.writeInt(6);
            out.writeInt(game.getGameID());

            //receive response
            String levelDataString = in.readUTF();
            LevelData levelData = Utility.getClassGson().fromJson(levelDataString, LevelData.class);

            //format game
            if (game.level == null) game.level = new Level(game, 0, 0);
            game.level.fromLevelData(levelData);
            if (game.hud == null) {
                game.hud = new HUD(game, game.level);
                game.hud.update();
            }
        }
    }

    @Override
    public void updateInput() throws IOException {
        synchronized (this) {
            //System.err.println("updateGame()");
            //write update game request
            out.writeInt(7);
            String input = "left:"+Game.leftkey+";";
            input += "right:"+Game.rightkey+";";
            input += "up:"+Game.upkey+";";
            input += "down:"+Game.downkey+";";
            input += "space:"+Game.spacekey+";";
            input += "d:"+Game.dkey+";";
            out.writeUTF(input);
        }
    }

    //lock server request commands for other threads
    public void lock() {
        locked = true;
    }

    //allow other threads to use commands
    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

}
