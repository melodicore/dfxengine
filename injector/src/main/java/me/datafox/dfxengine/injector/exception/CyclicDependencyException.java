package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when there are cyclic dependencies with {@link Component Components}.
 *
 * @author datafox
 */
public class CyclicDependencyException extends RuntimeException {
    /**
     * Public constructor for {@link CyclicDependencyException}.
     *
     * @param message details for this exception
     */
    public CyclicDependencyException(String message) {
        super(message);
    }
}
