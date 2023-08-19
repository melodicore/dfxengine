package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a cyclic dependency is present while {@link Component}s are being loaded.
 *
 * @author datafox
 */
public class CyclicDependencyException extends RuntimeException {
    public CyclicDependencyException() {
        super();
    }

    public CyclicDependencyException(String message) {
        super(message);
    }

    public CyclicDependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CyclicDependencyException(Throwable cause) {
        super(cause);
    }

    protected CyclicDependencyException(String message, Throwable cause,
                                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
