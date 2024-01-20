package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a single {@link Component} is requested but multiple valid Components are present.
 *
 * @author datafox
 */
public class MultipleValidComponentsException extends RuntimeException {
    /**
     * Constructor for MultipleValidComponentsException
     */
    public MultipleValidComponentsException() {
        super();
    }

    /**
     * Constructor for MultipleValidComponentsException
     *
     * @param message message for the exception
     */
    public MultipleValidComponentsException(String message) {
        super(message);
    }

    /**
     * Constructor for MultipleValidComponentsException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public MultipleValidComponentsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for MultipleValidComponentsException
     *
     * @param cause cause for the exception
     */
    public MultipleValidComponentsException(Throwable cause) {
        super(cause);
    }
}
