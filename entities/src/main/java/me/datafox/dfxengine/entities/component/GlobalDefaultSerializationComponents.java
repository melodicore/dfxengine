package me.datafox.dfxengine.entities.component;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonException;
import com.esotericsoftware.jsonbeans.JsonSerializer;
import com.esotericsoftware.jsonbeans.JsonValue;
import me.datafox.dfxengine.entities.serialization.ClassSerializerWrapper;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class GlobalDefaultSerializationComponents {
    @SuppressWarnings("rawtypes")
    @Component
    public static ClassSerializerWrapper<Class> classSerializer() {
        return new ClassSerializerWrapper<>(Class.class, new JsonSerializer<>() {
            @Override
            public void write(Json json, Class object, Class knownType) {
                json.writeValue(object.getName());
            }

            @Override
            public Class read(Json json, JsonValue jsonData, Class type) {
                try {
                    return Class.forName(jsonData.asString());
                } catch(ClassNotFoundException e) {
                    throw new JsonException("Could not find class " + jsonData.asString());
                }
            }
        });
    }
}
