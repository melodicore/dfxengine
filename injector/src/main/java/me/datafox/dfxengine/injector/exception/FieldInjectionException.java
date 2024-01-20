package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when a field annotated with {@link Inject} is not accessible.
 *
 * @author datafox
 */
public class FieldInjectionException extends RuntimeException {
    /**
     * Constructor for FieldInjectionException
     */
    public FieldInjectionException() {
        super();
    }

    /**
     * Constructor for FieldInjectionException
     *
     * @param message message for the exception
     */
    public FieldInjectionException(String message) {
        super(message);
    }

    /**
     * Constructor for FieldInjectionException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public FieldInjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for FieldInjectionException
     *
     * @param cause cause for the exception
     */
    public FieldInjectionException(Throwable cause) {
        super(cause);
    }
}
