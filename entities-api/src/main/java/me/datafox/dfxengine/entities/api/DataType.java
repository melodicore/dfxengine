package me.datafox.dfxengine.entities.api;

import lombok.Data;

/**
 * @author datafox
 */
@Data
public final class DataType<T> {
    private final Class<T> type;

    public String getId() {
        return type.getName();
    }

    public static <T> DataType<T> of(Class<T> type) {
        return new DataType<>(type);
    }
}
