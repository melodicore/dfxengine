package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when a class cannot be instantiated.
 *
 * @author datafox
 */
public class ClassInstantiationException extends RuntimeException {
    /**
     * Constructor for ClassInstantiationException
     */
    public ClassInstantiationException() {
        super();
    }

    /**
     * Constructor for ClassInstantiationException
     *
     * @param message message for the exception
     */
    public ClassInstantiationException(String message) {
        super(message);
    }

    /**
     * Constructor for ClassInstantiationException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public ClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for ClassInstantiationException
     *
     * @param cause cause for the exception
     */
    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }
}
