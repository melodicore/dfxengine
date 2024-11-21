package me.datafox.dfxengine.entities.component;

import com.esotericsoftware.jsonbeans.Json;
import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.ConfigurationManager;
import me.datafox.dfxengine.entities.api.component.SerializationHandlerConfiguration;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.io.IOException;
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
    public static final ConfigurationKey<Boolean> PRETTY_PRINT = ConfigurationKey.of(false);

    private final ConfigurationManager configurationManager;

    @Inject
    public DefaultSerializationHandler(List<SerializationHandlerConfiguration<Json, Json, DefaultSerializationHandler>> configurations, ConfigurationManager configurationManager) {
        super(Json::new, Function.identity(), configurations);
        this.configurationManager = configurationManager;
    }

    @Override
    public String serialize(Object object) {
        if(configurationManager.get(PRETTY_PRINT)) {
            return serializer.prettyPrint(object);
        } else {
            return serializer.toJson(object);
        }
    }

    @Override
    public void serialize(Object object, OutputStream stream) {
        if(configurationManager.get(PRETTY_PRINT)) {
            try(OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                writer.write(serializer.prettyPrint(object));
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            serializer.toJson(object, new OutputStreamWriter(stream));
        }
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
