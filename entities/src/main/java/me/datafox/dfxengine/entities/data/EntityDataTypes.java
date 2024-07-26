package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.api.DataType;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
@Component
public class EntityDataTypes {
    private static Map<String,Class<?>> types;

    @Inject
    public EntityDataTypes(List<DataType<?>> dataTypes) {
        types = new HashMap<>();
        dataTypes.stream()
                .map(DataType::getType)
                .forEach(EntityDataTypes::registerDataType);
    }

    public static void registerDataType(Class<?> type) {
        types.put(type.getName(), type);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getType(String id) {
        return (Class<T>) types.get(id);
    }

    @Component
    public static DataType<Value> valueType() {
        return DataType.of(Value.class);
    }

    @Component
    public static DataType<ValueMap> valueMapType() {
        return DataType.of(ValueMap.class);
    }
}
