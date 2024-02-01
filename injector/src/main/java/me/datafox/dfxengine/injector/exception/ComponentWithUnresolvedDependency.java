package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ComponentWithUnresolvedDependency extends RuntimeException {
    public ComponentWithUnresolvedDependency(String message) {
        super(message);
    }
}
