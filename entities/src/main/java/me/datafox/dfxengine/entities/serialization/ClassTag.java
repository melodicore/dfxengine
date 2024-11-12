package me.datafox.dfxengine.entities.serialization;

import lombok.Data;

/**
 * @author datafox
 */
@Data
public final class ClassTag {
    private final String tag;

    private final Class<?> type;
}
