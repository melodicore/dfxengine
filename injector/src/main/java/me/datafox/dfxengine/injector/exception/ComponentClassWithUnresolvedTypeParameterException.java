package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ComponentClassWithUnresolvedTypeParameterException extends RuntimeException {
    public ComponentClassWithUnresolvedTypeParameterException(String message) {
        super(message);
    }
}
