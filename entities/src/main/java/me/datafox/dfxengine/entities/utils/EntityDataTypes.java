package me.datafox.dfxengine.entities.utils;

import me.datafox.dfxengine.entities.api.DataType;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Component
public class EntityDataTypes {
    private static Map<String,Class<?>> types;

    @Inject
    public EntityDataTypes(List<DataType<?>> dataTypes) {
        types = new HashMap<>(dataTypes
                .stream()
                .collect(Collectors.toMap(
                        DataType::getId,
                        DataType::getType)));
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getType(String id) {
        return (Class<T>) types.get(id);
    }
}
