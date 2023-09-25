package me.datafox.dfxengine.injector.api;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * Determines how {@link Component Components} are instantiated. A Component with {@link #ONCE} is instantiated once as
 * a singleton when the Injector is initialized, while a Component with {@link #PER_INSTANCE} is instantiated for every
 * Component that depends on it.
 *
 * @author datafox
 */
public enum InstantiationPolicy {
    /**
     * Denotes that a {@link Component} will be instantiated once as a singleton.
     */
    ONCE,

    /**
     * Denotes that a {@link Component} will be instantiated for every Component that depends on it.
     */
    PER_INSTANCE
}
