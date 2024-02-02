package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ComponentWithUnresolvedTypeParameterException extends RuntimeException {
    public ComponentWithUnresolvedTypeParameterException(String message) {
        super(message);
    }
}
