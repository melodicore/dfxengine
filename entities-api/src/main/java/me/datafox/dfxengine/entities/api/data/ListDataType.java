package me.datafox.dfxengine.entities.api.data;

import java.util.List;

/**
 * @author datafox
 */
public interface ListDataType<T> extends DataType<List<T>> {
    @SuppressWarnings("unchecked")
    @Override
    default Class<List<T>> getType() {
        return (Class<List<T>>) (Class<?>) List.class;
    }

    @Override
    Class<T> getElementType();

    @Override
    default boolean isList() {
        return true;
    }

    @Override
    DataType<T> toSingle();

    @Override
    default DataType<List<T>> toList() {
        return this;
    }
}
