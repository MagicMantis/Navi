package net.magicmantis.src.exceptions;

/**
 * UnknownOptionException class: thrown when the user attempts to change an option not previously defined.
 */
public class UnknownOptionException extends Throwable {

    public UnknownOptionException() {
        super("Server cannot identify option requested.");
    }

}
