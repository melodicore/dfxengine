package me.datafox.dfxengine.injector.exception;

import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * An exception that is thrown when a {@link Component} depends on a single other Component or if a single Component has
 * been requested from the {@link Injector} but multiple are present that are not {@link Component#defaultImpl()} and
 * have the same lowest {@link Component#order()}.
 *
 * @author datafox
 */
public class MultipleDependenciesPresentException extends RuntimeException {
    public MultipleDependenciesPresentException(String message) {
        super(message);
    }
}
