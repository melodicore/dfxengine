package me.datafox.dfxengine.entities.serialization;

import com.esotericsoftware.jsonbeans.Json;
import me.datafox.dfxengine.entities.api.SerializationHandler;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class DefaultSerializationHandler implements SerializationHandler {
    private final Json json;
    private final boolean prettyPrint;

    @Inject
    public DefaultSerializationHandler(DefaultSerializationHandlerConfiguration configuration) {
        json = new Json(configuration.getOutputType());
        prettyPrint = configuration.isPrettyPrint();
        configuration.getTypes().forEach(e -> json.setElementType(e.getType(), e.getFieldName(), e.getElementType()));
    }

    @Override
    public String serialize(Object object) {
        return prettyPrint ? json.prettyPrint(object) : json.toJson(object);
    }

    @Override
    public <T> T deserialize(Class<T> type, String data) {
        return json.fromJson(type, data);
    }
}
