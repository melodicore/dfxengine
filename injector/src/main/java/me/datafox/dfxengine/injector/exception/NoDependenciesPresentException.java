package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.InjectorImpl;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a {@link Component} has dependencies that have not been registered, or if a single
 * Component is requested from the {@link InjectorImpl} and none are present.
 *
 * @author datafox
 */
public class NoDependenciesPresentException extends RuntimeException {
    public NoDependenciesPresentException(String message) {
        super(message);
    }
}
