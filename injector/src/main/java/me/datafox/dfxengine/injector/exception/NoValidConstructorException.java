package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when no constructor annotated with {@link Inject} and no default constructor is present
 * within a class to be instantiated.
 *
 * @author datafox
 */
public class NoValidConstructorException extends RuntimeException {
    public NoValidConstructorException() {
        super();
    }

    public NoValidConstructorException(String message) {
        super(message);
    }

    public NoValidConstructorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoValidConstructorException(Throwable cause) {
        super(cause);
    }

    protected NoValidConstructorException(String message, Throwable cause,
                                          boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
