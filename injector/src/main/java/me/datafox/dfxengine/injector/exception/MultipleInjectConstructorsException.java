package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when multiple constructors annotated with {@link Inject} are present within the same
 * class.
 *
 * @author datafox
 */
public class MultipleInjectConstructorsException extends RuntimeException {
    /**
     * Constructor for MultipleInjectConstructorsException
     */
    public MultipleInjectConstructorsException() {
        super();
    }

    /**
     * Constructor for MultipleInjectConstructorsException
     *
     * @param message message for the exception
     */
    public MultipleInjectConstructorsException(String message) {
        super(message);
    }

    /**
     * Constructor for MultipleInjectConstructorsException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public MultipleInjectConstructorsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for MultipleInjectConstructorsException
     *
     * @param cause cause for the exception
     */
    public MultipleInjectConstructorsException(Throwable cause) {
        super(cause);
    }
}
