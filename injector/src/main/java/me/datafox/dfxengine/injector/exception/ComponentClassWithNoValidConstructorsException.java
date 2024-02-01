package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ComponentClassWithNoValidConstructorsException extends RuntimeException {
    public ComponentClassWithNoValidConstructorsException(String message) {
        super(message);
    }
}
