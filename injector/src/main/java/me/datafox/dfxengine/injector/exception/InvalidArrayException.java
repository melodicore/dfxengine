package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when an unresolvable array is encountered.
 *
 * @author datafox
 */
public class InvalidArrayException extends RuntimeException {
    /**
     * Public constructor for {@link InvalidArrayException}.
     *
     * @param message details for this exception
     */
    public InvalidArrayException(String message) {
        super(message);
    }
}
