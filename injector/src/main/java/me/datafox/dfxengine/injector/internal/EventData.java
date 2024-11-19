package me.datafox.dfxengine.injector.internal;

import lombok.Builder;
import lombok.Data;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;

import java.lang.reflect.Method;

/**
 * A reference to a {@link EventHandler} {@link Method} used internally by this module.
 *
 * @param <T> type of the referenced {@link EventHandler} {@link Method}
 *
 * @author datafox
 */
@Builder
@Data
@SuppressWarnings("MissingJavadoc")
public class EventData<T> {
    private final ClassReference<T> event;

    private final ClassReference<?> owner;

    private final Method method;
}
