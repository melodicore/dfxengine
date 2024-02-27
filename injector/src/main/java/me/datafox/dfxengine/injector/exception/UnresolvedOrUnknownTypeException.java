package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class UnresolvedOrUnknownTypeException extends RuntimeException {
    public UnresolvedOrUnknownTypeException(String message) {
        super(message);
    }

    public UnresolvedOrUnknownTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
