package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a {@link Component} has a defaultFor value that is not a superclass or
 * superinterface of the component class.
 *
 * @author datafox
 */
public class InvalidDefaultTypeException extends RuntimeException {
    public InvalidDefaultTypeException() {
        super();
    }

    public InvalidDefaultTypeException(String message) {
        super(message);
    }

    public InvalidDefaultTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDefaultTypeException(Throwable cause) {
        super(cause);
    }

    protected InvalidDefaultTypeException(String message, Throwable cause,
                                          boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
