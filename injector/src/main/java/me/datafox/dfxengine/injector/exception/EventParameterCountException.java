package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.EventHandler;

/**
 * An exception that is thrown when an {@link EventHandler} method does not have exactly one parameter.
 *
 * @author datafox
 */
public class EventParameterCountException extends RuntimeException {
    public EventParameterCountException(String message) {
        super(message);
    }
}
