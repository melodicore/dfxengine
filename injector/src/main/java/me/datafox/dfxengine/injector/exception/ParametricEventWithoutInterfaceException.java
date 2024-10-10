package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.ParametricEvent;

/**
 * An exception that is thrown when {@link Injector#invokeEvent(Object)} is called with an object that has type
 * parameters but does not implement {@link ParametricEvent}.
 *
 * @author datafox
 */
public class ParametricEventWithoutInterfaceException extends RuntimeException {
    public ParametricEventWithoutInterfaceException(String message) {
        super(message);
    }
}
