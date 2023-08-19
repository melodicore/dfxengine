package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when a field annotated with {@link Inject} is not accessible.
 *
 * @author datafox
 */
public class FieldInjectionException extends RuntimeException {
    public FieldInjectionException() {
        super();
    }

    public FieldInjectionException(String message) {
        super(message);
    }

    public FieldInjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldInjectionException(Throwable cause) {
        super(cause);
    }

    protected FieldInjectionException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
