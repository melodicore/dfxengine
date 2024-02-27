package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class ParameterCountMismatchException extends RuntimeException {
    public ParameterCountMismatchException(String message) {
        super(message);
    }

    public ParameterCountMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
