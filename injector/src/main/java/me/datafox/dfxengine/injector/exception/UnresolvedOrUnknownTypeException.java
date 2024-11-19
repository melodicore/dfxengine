package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a {@link Component} or any of its dependencies have an unresolved or unknown type.
 *
 * @author datafox
 */
public class UnresolvedOrUnknownTypeException extends RuntimeException {
    /**
     * Public constructor for {@link UnresolvedOrUnknownTypeException}.
     *
     * @param message details for this exception
     */
    public UnresolvedOrUnknownTypeException(String message) {
        super(message);
    }
}
