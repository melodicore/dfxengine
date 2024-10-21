package me.datafox.dfxengine.entities.api.data;

import java.util.List;

/**
 * @author datafox
 */
public interface SingleDataType<T> extends DataType<T> {
    @Override
    default Class<T> getElementType() {
        return getType();
    }

    @Override
    default boolean isList() {
        return false;
    }

    @Override
    default DataType<T> toSingle() {
        return this;
    }

    @Override
    DataType<List<T>> toList();
}
