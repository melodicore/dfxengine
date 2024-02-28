package me.datafox.dfxengine.injector.internal;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

import java.lang.reflect.Method;
import java.util.List;

/**
 * A reference to an {@link Initialize} {@link Method} and its type and parameters, used internally by this module.
 *
 * @author datafox
 */
@Builder
@Data
public class InitializeReference<T> {
    private final int priority;

    private final ClassReference<T> reference;

    private final Method method;

    @Singular
    private final List<ClassReference<?>> parameters;
}
