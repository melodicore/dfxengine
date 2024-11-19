package me.datafox.dfxengine.injector.internal;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * A reference to a {@link Field} and its type, used internally by this module.
 *
 * @param <T> type of the {@link Class} that holds the referenced {@link Field}
 *
 * @author datafox
 */
@Builder
@Data
@SuppressWarnings("MissingJavadoc")
public class FieldReference<T> {
    private final Field field;

    private final ClassReference<T> reference;
}
