package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a method annotated with {@link Component} returns an array.
 *
 * @author datafox
 */
public class ArrayComponentException extends RuntimeException {
    public ArrayComponentException(String message) {
        super(message);
    }
}
