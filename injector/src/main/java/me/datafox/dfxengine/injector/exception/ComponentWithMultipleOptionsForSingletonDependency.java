package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ComponentWithMultipleOptionsForSingletonDependency extends RuntimeException {
    public ComponentWithMultipleOptionsForSingletonDependency(String message) {
        super(message);
    }
}
