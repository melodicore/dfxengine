package me.datafox.dfxengine.entities.configuration;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;
import me.datafox.dfxengine.entities.api.SerializationHandler;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class JsonConfiguration {
    @Component(order = Integer.MAX_VALUE)
    public static SerializationHandler getDefaultSerializationHandler() {
        return new DefaultSerializationHandler();
    }

    public static class DefaultSerializationHandler implements SerializationHandler {
        private final Json json;

        public DefaultSerializationHandler() {
            json = new Json(OutputType.json);
        }

        @Override
        public String serialize(Object object) {
            return json.toJson(object);
        }

        @Override
        public <T> T deserialize(Class<T> type, String data) {
            return json.fromJson(type, data);
        }
    }
}
