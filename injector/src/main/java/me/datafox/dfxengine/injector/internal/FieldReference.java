package me.datafox.dfxengine.injector.internal;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author datafox
 */
@Builder
@Data
public class FieldReference<T> {
    private final Field field;

    private final ClassReference<T> reference;
}
