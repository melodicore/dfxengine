package me.datafox.dfxengine.entities.serialization;

import com.esotericsoftware.jsonbeans.OutputType;
import lombok.Builder;
import lombok.Data;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Data
@Builder
public class DefaultSerializationHandlerConfiguration {
    private final OutputType outputType;
    private final boolean prettyPrint;

    @Component(order = Integer.MAX_VALUE)
    public static DefaultSerializationHandlerConfiguration defaultConfiguration() {
        return new DefaultSerializationHandlerConfiguration(OutputType.json, false);
    }
}
