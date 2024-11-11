package me.datafox.dfxengine.entities.api.data;

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
    default SingleDataType<T> toSingle() {
        return this;
    }

    @Override
    ListDataType<T> toList();
}
