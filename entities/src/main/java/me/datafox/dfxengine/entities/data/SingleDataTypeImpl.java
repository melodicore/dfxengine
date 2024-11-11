package me.datafox.dfxengine.entities.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.data.SingleDataType;

/**
 * @author datafox
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleDataTypeImpl<T> implements SingleDataType<T> {
    private Class<T> type;

    private int variation;

    @Override
    public ListDataType<T> toList() {
        return ListDataTypeImpl.of(type, variation);
    }

    @Override
    public boolean canCast(Object o) {
        return type.isInstance(o);
    }

    public static <T> SingleDataTypeImpl<T> of(Class<T> type, int variation) {
        return new SingleDataTypeImpl<>(type, variation);
    }

    public static <T> SingleDataTypeImpl<T> of(Class<T> type) {
        return of(type, 0);
    }
}
