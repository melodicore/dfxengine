package me.datafox.dfxengine.injector;

import lombok.*;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.List;

/**
 * Contains instantiation details of an object when using the {@link Injector}. It contains a reference to the class the
 * {@link Component} was requested with, as well as the class depending on said Component. Because of this, it is only
 * useful for Components that have {@link InstantiationPolicy#PER_INSTANCE}. For Components that have
 * {@link InstantiationPolicy#ONCE}, {@link #requestingType} will always be {@code null}.
 *
 * @author datafox
 */
@Builder
@Data
public final class InstantiationDetails {
    private final Class<?> type;

    @Singular
    private final List<Parameter<?>> parameters;

    private final Class<?> requestingType;

    @Singular
    private final List<Parameter<?>> requestingParameters;
}
