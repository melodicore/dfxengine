package me.datafox.dfxengine.injector.api.exception;

import me.datafox.dfxengine.injector.api.TypeRef;

/**
 * An exception that is thrown when a {@link TypeRef} is instantiated with the count of parameters not matching with the
 * associated {@link Class}.
 *
 * @author datafox
 */
public class ParameterCountMismatchException extends RuntimeException {
    public ParameterCountMismatchException(String message) {
        super(message);
    }
}
