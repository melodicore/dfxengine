package me.datafox.dfxengine.entities.utils;

import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;

import java.util.List;

/**
 * @author datafox
 */
public class TypeUtils {
    @SuppressWarnings("unchecked")
    public static <T> List<T> castToList(NodeData<?> data, Class<T> elementType) {
        if(!elementType.equals(data.getType().getElementType())) {
            throw new ClassCastException();
        }
        if(data.getType().isList()) {
            return (List<T>) data.getData();
        } else {
            return List.of((T) data.getData());
        }
    }
}
