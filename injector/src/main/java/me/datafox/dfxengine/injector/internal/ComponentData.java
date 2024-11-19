package me.datafox.dfxengine.injector.internal;

import lombok.*;
import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.lang.reflect.Executable;
import java.util.List;

/**
 * A reference to a {@link Component} used internally by this module.
 *
 * @param <T> type of the referenced {@link Component}
 *
 * @author datafox
 */
@Builder
@Data
@SuppressWarnings("MissingJavadoc")
public class ComponentData<T> {
    private final ClassReference<T> reference;

    private final InstantiationPolicy policy;

    private final int order;

    private final ClassReference<?> owner;

    private final Executable executable;

    @Singular
    private final List<ClassReference<?>> parameters;

    @Singular
    private final List<FieldReference<?>> fields;

    @Singular
    private final List<InitializeReference<?>> initializers;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<List<ComponentData<?>>> dependencies;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<T> objects;
}
