package me.datafox.dfxengine.entities.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.List;

/**
 * @author datafox
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDataTypeImpl<T> implements ListDataType<T> {
    private Class<T> elementType;

    private int variation;

    @Override
    public SingleDataType<T> toSingle() {
        return SingleDataTypeImpl.of(elementType, variation);
    }

    @Override
    public boolean canCast(Object o) {
        if(!(o instanceof List)) {
            return false;
        }
        return ((List<?>) o).stream().allMatch(elementType::isInstance);
    }

    public static <T> ListDataTypeImpl<T> of(Class<T> type) {
        return of(type, 0);
    }

    public static <T> ListDataTypeImpl<T> of(Class<T> type, int variation) {
        return new ListDataTypeImpl<>(type, variation);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("list", ListDataTypeImpl.class);
    }
}
