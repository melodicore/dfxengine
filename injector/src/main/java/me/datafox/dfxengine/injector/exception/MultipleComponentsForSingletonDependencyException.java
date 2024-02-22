package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class MultipleComponentsForSingletonDependencyException extends RuntimeException {
    public MultipleComponentsForSingletonDependencyException(String message) {
        super(message);
    }
}
