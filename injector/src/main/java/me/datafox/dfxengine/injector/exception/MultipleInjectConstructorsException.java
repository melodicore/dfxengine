package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when multiple constructors annotated with {@link Inject} are present within the same
 * class.
 *
 * @author datafox
 */
public class MultipleInjectConstructorsException extends RuntimeException {
    public MultipleInjectConstructorsException() {
        super();
    }

    public MultipleInjectConstructorsException(String message) {
        super(message);
    }

    public MultipleInjectConstructorsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleInjectConstructorsException(Throwable cause) {
        super(cause);
    }

    protected MultipleInjectConstructorsException(String message, Throwable cause,
                                                  boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
