package me.datafox.dfxengine.entities.serialization;

import lombok.Data;

/**
 * @author datafox
 */
@Data
public final class DefaultElement {
    private final Class<?> type;

    private final String fieldName;

    private final Class<?> elementType;
}
