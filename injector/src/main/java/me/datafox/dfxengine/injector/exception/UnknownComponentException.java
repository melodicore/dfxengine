package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when no components are present for the requested type.
 *
 * @author datafox
 */
public class UnknownComponentException extends RuntimeException {
    public UnknownComponentException() {
        super();
    }

    public UnknownComponentException(String message) {
        super(message);
    }

    public UnknownComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownComponentException(Throwable cause) {
        super(cause);
    }

    protected UnknownComponentException(String message, Throwable cause,
                                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
