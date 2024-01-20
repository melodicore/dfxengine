package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when no constructor annotated with {@link Inject} and no default constructor is present
 * within a class to be instantiated.
 *
 * @author datafox
 */
public class NoValidConstructorException extends RuntimeException {
    /**
     * Constructor for FieldInjectionException
     */
    public NoValidConstructorException() {
        super();
    }

    /**
     * Constructor for FieldInjectionException
     *
     * @param message message for the exception
     */
    public NoValidConstructorException(String message) {
        super(message);
    }

    /**
     * Constructor for FieldInjectionException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public NoValidConstructorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for FieldInjectionException
     *
     * @param cause cause for the exception
     */
    public NoValidConstructorException(Throwable cause) {
        super(cause);
    }
}
