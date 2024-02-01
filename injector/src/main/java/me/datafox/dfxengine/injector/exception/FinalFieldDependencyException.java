package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class FinalFieldDependencyException extends RuntimeException {
    public FinalFieldDependencyException() {
        super();
    }

    public FinalFieldDependencyException(String message) {
        super(message);
    }
}
