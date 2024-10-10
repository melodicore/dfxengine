package me.datafox.dfxengine.injector.internal;

import lombok.Builder;
import lombok.Data;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.lang.reflect.Method;

/**
 * A reference to a {@link Component} used internally by this module.
 *
 * @author datafox
 */
@Builder
@Data
public class EventData<T> {
    private final ClassReference<T> event;

    private final ClassReference<?> returnedEvent;

    private final ClassReference<?> owner;

    private final Method method;
}
