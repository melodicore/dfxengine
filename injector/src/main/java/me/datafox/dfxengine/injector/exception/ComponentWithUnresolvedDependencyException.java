package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ComponentWithUnresolvedDependencyException extends RuntimeException {
    public ComponentWithUnresolvedDependencyException(String message) {
        super(message);
    }
}
