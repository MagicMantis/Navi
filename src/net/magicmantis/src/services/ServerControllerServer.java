package net.magicmantis.src.services;

import com.google.gson.Gson;
import net.magicmantis.src.exceptions.FailedStartGameException;
import net.magicmantis.src.exceptions.GameNotFoundException;
import net.magicmantis.src.exceptions.UnknownOptionException;
import net.magicmantis.src.exceptions.AccessDeniedException;
import net.magicmantis.src.server.OnlineGame;
import net.magicmantis.src.server.User;
import net.magicmantis.src.server.dataStructures.LevelData;
import net.magicmantis.src.services.ServerController;
import net.magicmantis.src.services.Utility;
import org.lwjgl.system.CallbackI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by Joseph on 5/4/2015.
 */

public class ServerControllerServer implements ServerController {

    private boolean locked;
    private ArrayList<OnlineGame> games;
    private User user;
    private int gameIDs;

    public ServerControllerServer(ArrayList<OnlineGame> games, User user) {
        this.games = games;
        this.user = user;
        this.gameIDs = 0;
        this.locked = false;
    }

    //unused
    @Override
    public void disconnect() throws IOException {

    }

    @Override
    public void connect() throws IOException {
        System.out.println("connect() "+user.getID());
        user.getOutput().writeInt(user.getID());
    }

    @Override
    public void authenticate() {

    }

    @Override
    public void matchMake() throws IOException {
        System.out.println("matchMake()");
        OnlineGame match = null;
        if (games.size() > 0) {
            for (OnlineGame g: games) {
                if (!g.isFull() && g.isJoinable()) match = g;
            }
        }
        if (match == null) {
            match = new OnlineGame(gameIDs, 8);
            gameIDs++;
            games.add(match);
        }
        match.addPlayer(user);
        user.setGame(match);
        user.getOutput().writeInt(match.getID());
    }

    @Override
    public OnlineGame getGameInfo() throws IOException, GameNotFoundException {
        int gameID = user.getInput().readInt();
        OnlineGame game = null;
        for (OnlineGame g : games) {
            if (g.getID() == gameID) game = g;
        }
        if (game == null) throw new GameNotFoundException();
        user.getOutput().writeInt(game.getID());
        user.getOutput().writeInt(game.getMaxPlayers());
        user.getOutput().writeInt(game.getPlayerCount());
        user.getOutput().writeUTF(new Gson().toJson(game.getUserData()));
        user.getOutput().writeUTF(new Gson().toJson(game.getOptions()));
        user.getOutput().writeBoolean(game.isStarted());
        return null;
    }

    @Override
    public void changeOption(String option, Object value) throws IOException, ClassNotFoundException, UnknownOptionException, AccessDeniedException {
        System.out.println("changeOption()");
        //read game id and verify permission
        int gameID = user.getInput().readInt();
        if (gameID != user.getGame().getID() || user.getGame().isStarted()) throw new AccessDeniedException();

        //read the option parameters
        String optionString = user.getInput().readUTF();
        ObjectInputStream ois = new ObjectInputStream(user.getInput());
        Object valueObject = ois.readObject();
        if (optionString.equals("allowTeams")) {
            user.getGame().getOptions().put("allowTeams", valueObject);
        }
        else if (optionString.equals("spawnFactories")) {
            user.getGame().getOptions().put("spawnFactories", valueObject);
        }
        else {
            throw new UnknownOptionException();
        }

        user.getOutput().writeInt(0);
    }

    @Override
    public void changeTeam(int playerID, boolean up) throws IOException, AccessDeniedException {
        System.out.println("changeTeam()");
        //read game id and verify permission
        int gameID = user.getInput().readInt();
        if (gameID != user.getGame().getID() || user.getGame().isStarted()) throw new AccessDeniedException();

        //read the team change parameters
        int requestedPlayerID = user.getInput().readInt();
        boolean direction = user.getInput().readBoolean();

        //change the team
        user.getGame().changeTeam(requestedPlayerID, direction);

        //send successful status response
        user.getOutput().writeInt(0);
    }

    @Override
    public void startGame() throws IOException, FailedStartGameException {
        System.out.println("startGame()");
        int gameID = user.getInput().readInt();
        if (gameID != user.getGame().getID() || user.getGame().isStarted() || !user.getGame().start()) throw new FailedStartGameException();
        user.getOutput().writeInt(0);
    }

    @Override
    public void getLevel() throws IOException, GameNotFoundException {
        synchronized (this) {
            int gameID = user.getInput().readInt();
            if (gameID != user.getGame().getID()) throw new GameNotFoundException();

            //send level
            LevelData levelData = new LevelData(user.getGame().getLevel(), user.getID());
            user.getOutput().writeUTF(Utility.getClassGson().toJson(levelData));
        }
    }

    @Override
    public void updateInput() throws IOException {
        //System.out.println("getGameInfo()");
        String input = user.getInput().readUTF();
        while (input.length() > 0) {
            user.userInput.put(input.substring(0, input.indexOf(':')),
                    Boolean.valueOf(input.substring(input.indexOf(':') + 1, input.indexOf(';'))));
            input = input.substring(input.indexOf(';')+1);
        }
    }

    @Override
    public void lock() {
        locked = true;
    }

    @Override
    public void unlock() {
        locked = false;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
