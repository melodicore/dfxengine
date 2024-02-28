package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * An exception that is thrown when a class annotated with {@link Component} has multiple constructors annotated with
 * {@link Inject}.
 *
 * @author datafox
 */
public class ComponentClassWithMultipleValidConstructorsException extends RuntimeException {
    public ComponentClassWithMultipleValidConstructorsException(String message) {
        super(message);
    }
}
