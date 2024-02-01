package me.datafox.dfxengine.injector.internal;

import lombok.*;

import java.lang.reflect.Method;
import java.util.List;

/**
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
