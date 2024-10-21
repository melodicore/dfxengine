package me.datafox.dfxengine.entities.api.data;

/**
 * @author datafox
 */
public interface DataType<T> {
    Class<T> getType();

    Class<?> getElementType();

    int getVariation();

    boolean isList();

    DataType<?> toSingle();

    DataType<?> toList();

    boolean canCast(Object o);

    @SuppressWarnings("unchecked")
    default T cast(Object o) {
        if(!canCast(o)) {
            throw new ClassCastException();
        }
        return (T) o;
    }
}
