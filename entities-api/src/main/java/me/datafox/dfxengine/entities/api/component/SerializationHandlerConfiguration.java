package me.datafox.dfxengine.entities.api.component;

/**
 * @author datafox
 */
@FunctionalInterface
public interface SerializationHandlerConfiguration<B, T, H extends SerializationHandler<B, T>> {
    B configure(B builder);
}
