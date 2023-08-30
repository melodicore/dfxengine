package me.datafox.dfxengine.injector;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;

/**
 * Contains instantiation details of an object when using the {@link Injector}. It contains a reference to the class the
 * component was requested with, as well as the class depending on said component. Because of this, it is only useful
 * for components that have {@link InstantiationPolicy#PER_INSTANCE}. For components that have
 * {@link InstantiationPolicy#ONCE}, {@link #getType()} will always be the declaring class of the component, and
 * {@link #getRequestingType()} will always be null.
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
