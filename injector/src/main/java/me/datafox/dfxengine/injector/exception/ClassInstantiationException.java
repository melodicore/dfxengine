package me.datafox.dfxengine.injector.exception;

/**
 * An exception that is thrown when a class cannot be instantiated.
 *
 * @author datafox
 */
public class ClassInstantiationException extends RuntimeException {
    public ClassInstantiationException() {
        super();
    }

    public ClassInstantiationException(String message) {
        super(message);
    }

    public ClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }

    protected ClassInstantiationException(String message, Throwable cause,
                                          boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
