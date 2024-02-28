package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when a class annotated with {@link Component} has no constructors annotated with
 * {@link Inject} and no default constructor.
 *
 * @author datafox
 */
public class ComponentClassWithNoValidConstructorsException extends RuntimeException {
    public ComponentClassWithNoValidConstructorsException(String message) {
        super(message);
    }
}
