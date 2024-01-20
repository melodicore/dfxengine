package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when a method cannot be invoked.
 *
 * @author datafox
 */
public class MethodInvocationException extends RuntimeException {
    /**
     * Constructor for MethodInvocationException
     */
    public MethodInvocationException() {
        super();
    }

    /**
     * Constructor for MethodInvocationException
     *
     * @param message message for the exception
     */
    public MethodInvocationException(String message) {
        super(message);
    }

    /**
     * Constructor for MethodInvocationException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public MethodInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for MethodInvocationException
     *
     * @param cause cause for the exception
     */
    public MethodInvocationException(Throwable cause) {
        super(cause);
    }
}
