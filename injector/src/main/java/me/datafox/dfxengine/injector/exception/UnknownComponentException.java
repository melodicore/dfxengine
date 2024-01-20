package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when no {@link Component Components} are present for the requested type.
 *
 * @author datafox
 */
public class UnknownComponentException extends RuntimeException {
    /**
     * Constructor for UnknownComponentException
     */
    public UnknownComponentException() {
        super();
    }

    /**
     * Constructor for UnknownComponentException
     *
     * @param message message for the exception
     */
    public UnknownComponentException(String message) {
        super(message);
    }

    /**
     * Constructor for UnknownComponentException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public UnknownComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for UnknownComponentException
     *
     * @param cause cause for the exception
     */
    public UnknownComponentException(Throwable cause) {
        super(cause);
    }
}
