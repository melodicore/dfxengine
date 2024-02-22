package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ArrayComponentException extends RuntimeException {
    public ArrayComponentException(String message) {
        super(message);
    }

    public ArrayComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}
