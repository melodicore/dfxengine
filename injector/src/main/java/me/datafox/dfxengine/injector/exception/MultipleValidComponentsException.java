package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when a single component is requested but multiple valid components are present.
 *
 * @author datafox
 */
public class MultipleValidComponentsException extends RuntimeException {
    public MultipleValidComponentsException() {
        super();
    }

    public MultipleValidComponentsException(String message) {
        super(message);
    }

    public MultipleValidComponentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleValidComponentsException(Throwable cause) {
        super(cause);
    }

    protected MultipleValidComponentsException(String message, Throwable cause,
                                               boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
