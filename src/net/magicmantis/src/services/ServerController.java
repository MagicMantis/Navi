package net.magicmantis.src.services;

import net.magicmantis.src.exceptions.AccessDeniedException;
import net.magicmantis.src.exceptions.FailedStartGameException;
import net.magicmantis.src.exceptions.GameNotFoundException;
import net.magicmantis.src.exceptions.UnknownOptionException;
import net.magicmantis.src.server.OnlineGame;
import net.magicmantis.src.server.User;

import java.io.IOException;

/**
 * Created by Joseph on 5/4/2015.
 */
public interface ServerController {

    //server interface API
    public void disconnect() throws IOException;
    public void connect() throws IOException;
    public void authenticate() throws IOException;
    public void matchMake() throws IOException, GameNotFoundException;
    public OnlineGame getGameInfo() throws IOException, GameNotFoundException;
    public void changeOption(String option, Object value) throws IOException, ClassNotFoundException, UnknownOptionException, AccessDeniedException;
    public void changeTeam(int playerID, boolean up) throws IOException, AccessDeniedException;
    public void startGame() throws IOException, FailedStartGameException;
    public void getLevel() throws IOException, GameNotFoundException;
    public void updateInput() throws IOException, GameNotFoundException;
    public void getResults() throws IOException, GameNotFoundException;

    //synchronization
    public void lock();
    public void unlock();
    public boolean isLocked();

}
