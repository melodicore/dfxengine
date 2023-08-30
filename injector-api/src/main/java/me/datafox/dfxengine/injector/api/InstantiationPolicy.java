package me.datafox.dfxengine.injector.api;

/**
 * Determines how components are instantiated. A component with {@link #ONCE} is instantiated once as a singleton when
 * the injector is initialized, while a component with {@link #PER_INSTANCE} is instantiated for every component that
 * depends on it.
 *
 * @author datafox
 */
public enum InstantiationPolicy {
    /**
     * Denotes that a component will be instantiated once as a singleton.
     */
    ONCE,

    /**
     * Denotes that a component will be instantiated for every component that depends on it.
     */
    PER_INSTANCE
}
