package me.datafox.dfxengine.entities.exception;

/**
 * @author datafox
 */
public class InvalidNodeTreeException extends RuntimeException {
    public InvalidNodeTreeException() {
        super();
    }

    public InvalidNodeTreeException(String message) {
        super(message);
    }

    public InvalidNodeTreeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNodeTreeException(Throwable cause) {
        super(cause);
    }

    protected InvalidNodeTreeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
