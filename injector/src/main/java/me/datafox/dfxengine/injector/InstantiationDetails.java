package me.datafox.dfxengine.injector;

import lombok.Builder;
import lombok.Data;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * Contains instantiation details of an object when using the {@link Injector}. It contains a reference to the class the
 * {@link Component} was requested with, as well as the class depending on said Component. Because of this, it is only
 * useful for Components that have {@link InstantiationPolicy#PER_INSTANCE}. For Components that have
 * {@link InstantiationPolicy#ONCE}, {@link #requesting} will always be {@code null}.
 *
 * @author datafox
 */
@Builder
@Data
public final class InstantiationDetails {
    private final TypeRef<?> type;

    private final TypeRef<?> requesting;
}
