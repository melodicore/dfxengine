package me.datafox.dfxengine.injector.api;

/**
 * An interface for events that have type parameters. If an event object has type parameters and is passed to
 * {@link Injector#invokeEvent(Object)} without it implementing this interface, an exception is thrown.
 *
 * @author datafox
 */
public interface ParametricEvent {
    /**
     * Returns the {@link TypeRef} representing the type of the implementing class.
     *
     * @return {@link TypeRef} representing the type of the implementing class
     */
    TypeRef<?> getType();
}
