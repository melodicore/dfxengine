package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a single {@link Component} is requested but multiple valid Components are present.
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
