package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a {@link Component} has a defaultFor value that is not a superclass or
 * superinterface of the component class.
 *
 * @author datafox
 */
public class InvalidDefaultTypeException extends RuntimeException {
    /**
     * Constructor for InvalidDefaultTypeException
     */
    public InvalidDefaultTypeException() {
        super();
    }

    /**
     * Constructor for InvalidDefaultTypeException
     *
     * @param message message for the exception
     */
    public InvalidDefaultTypeException(String message) {
        super(message);
    }

    /**
     * Constructor for InvalidDefaultTypeException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public InvalidDefaultTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for InvalidDefaultTypeException
     *
     * @param cause cause for the exception
     */
    public InvalidDefaultTypeException(Throwable cause) {
        super(cause);
    }
}
