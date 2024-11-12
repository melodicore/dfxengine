package me.datafox.dfxengine.entities.component;

import com.esotericsoftware.jsonbeans.Json;
import me.datafox.dfxengine.entities.api.component.SerializationHandlerConfiguration;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.function.Function;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class DefaultSerializationHandler extends SerializationHandlerBase<Json,Json, DefaultSerializationHandler> {
    @Inject
    public DefaultSerializationHandler(List<SerializationHandlerConfiguration<Json, Json, DefaultSerializationHandler>> configurations) {
        super(Json::new, Function.identity(), configurations);
    }

    @Override
    public String serialize(Object object) {
        return serializer.toJson(object);
    }

    @Override
    public void serialize(Object object, OutputStream stream) {
        serializer.toJson(object, new OutputStreamWriter(stream));
    }

    @Override
    public <E> E deserialize(Class<E> type, String data) {
        return serializer.fromJson(type, data);
    }

    @Override
    public <E> E deserialize(Class<E> type, InputStream stream) {
        return serializer.fromJson(type, stream);
    }
}
