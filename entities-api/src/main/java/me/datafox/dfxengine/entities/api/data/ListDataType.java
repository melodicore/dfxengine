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
    SingleDataType<T> toSingle();

    @Override
    default ListDataType<T> toList() {
        return this;
    }
}
