package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ComponentClassWithMultipleValidConstructorsException extends RuntimeException {
    public ComponentClassWithMultipleValidConstructorsException(String message) {
        super(message);
    }
}
