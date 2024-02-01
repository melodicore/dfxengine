package me.datafox.dfxengine.injector.exception;

/**
 * @author datafox
 */
public class UnknownComponentTypeParameterException extends RuntimeException {
    public UnknownComponentTypeParameterException() {
        super();
    }

    public UnknownComponentTypeParameterException(String message) {
        super(message);
    }
}
