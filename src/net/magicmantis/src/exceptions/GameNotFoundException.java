package net.magicmantis.src.exceptions;

/**
 * GameNotFoundException class: thrown when a game could not be found
 */
public class GameNotFoundException extends Exception {

    public GameNotFoundException() {
        super("Game could not be found.");
    }

}
