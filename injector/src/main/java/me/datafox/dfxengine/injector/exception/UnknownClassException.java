package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class UnknownClassException extends RuntimeException {
    public UnknownClassException() {
        super();
    }

    public UnknownClassException(String message) {
        super(message);
    }

    public UnknownClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
