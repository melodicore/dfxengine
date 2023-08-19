package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when a method cannot be invoked.
 *
 * @author datafox
 */
public class MethodInvocationException extends RuntimeException {
    public MethodInvocationException() {
        super();
    }

    public MethodInvocationException(String message) {
        super(message);
    }

    public MethodInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodInvocationException(Throwable cause) {
        super(cause);
    }

    protected MethodInvocationException(String message, Throwable cause,
                                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
