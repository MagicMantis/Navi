package net.magicmantis.src.exceptions;

/**
 * AccessDeniedException class: thrown when a user tries a forbidden action
 */
public class AccessDeniedException extends Throwable {

    public AccessDeniedException() {
        super("Server denied access to the requested function.");
    }
}
