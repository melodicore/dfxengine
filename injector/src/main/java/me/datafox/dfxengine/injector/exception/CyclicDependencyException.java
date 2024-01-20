package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a cyclic dependency is present while {@link Component Components} are being loaded.
 *
 * @author datafox
 */
public class CyclicDependencyException extends RuntimeException {
    /**
     * Constructor for CyclicDependencyException
     */
    public CyclicDependencyException() {
        super();
    }

    /**
     * Constructor for CyclicDependencyException
     *
     * @param message message for the exception
     */
    public CyclicDependencyException(String message) {
        super(message);
    }

    /**
     * Constructor for CyclicDependencyException
     *
     * @param message message for the exception
     * @param cause cause for the exception
     */
    public CyclicDependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for CyclicDependencyException
     *
     * @param cause cause for the exception
     */
    public CyclicDependencyException(Throwable cause) {
        super(cause);
    }
}
