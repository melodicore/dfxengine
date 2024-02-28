package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when a field annotated with {@link Inject} is {@code final}.
 *
 * @author datafox
 */
public class FinalFieldDependencyException extends RuntimeException {
    public FinalFieldDependencyException(String message) {
        super(message);
    }
}
