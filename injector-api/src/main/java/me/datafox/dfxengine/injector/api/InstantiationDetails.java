package me.datafox.dfxengine.injector.api;

import lombok.Builder;
import lombok.Data;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * Contains instantiation details of an object when using the {@link Injector}. When used as a dependency, it contains a
 * reference to the class the {@link Component} was requested with, as well as the declared class of said Component.
 * For Components that have {@link InstantiationPolicy#ONCE}, {@link #requesting} will always be {@code null}.
 *
 * @author datafox
 */
@Builder
@Data
public final class InstantiationDetails {
    /**
     * {@link TypeRef} of the instantiated component.
     */
    private final TypeRef<?> type;

    /**
     * {@link TypeRef} of the component that requested the instantiated component.
     */
    private final TypeRef<?> requesting;
}
