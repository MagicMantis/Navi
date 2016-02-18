package net.magicmantis.src.exceptions;

/**
 * FailedStartGameException class: thrown when a game start attempt fails
 */
public class FailedStartGameException extends Throwable {

    public FailedStartGameException() {
        super("Failed to start the specified game.");
    }

}
