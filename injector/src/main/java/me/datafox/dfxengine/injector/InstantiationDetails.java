package me.datafox.dfxengine.injector;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * Contains instantiation details of an object when using the {@link Injector}. It contains a reference to the class the
 * {@link Component} was requested with, as well as the class depending on said Component. Because of this, it is only
 * useful for Components that have {@link InstantiationPolicy#PER_INSTANCE}. For Components that have
 * {@link InstantiationPolicy#ONCE}, {@link #type} will always be the declaring class of the component, and
 * {@link #requestingType} will always be {@code null}.
 *
 * @author datafox
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class InstantiationDetails<T,R> {
    private final Class<T> type;

    private final Class<R> requestingType;

    public static <T,R> InstantiationDetails<T,R> of(Class<T> type, Class<R> requestingType) {
        return new InstantiationDetails<>(type, requestingType);
    }
}
