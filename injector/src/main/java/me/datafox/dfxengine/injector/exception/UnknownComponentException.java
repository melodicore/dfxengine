package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when no {@link Component Components} are present for the requested type.
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
