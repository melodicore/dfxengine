package me.datafox.dfxengine.entities.serialization;

import com.esotericsoftware.jsonbeans.OutputType;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import me.datafox.dfxengine.entities.DataPackImpl;
import me.datafox.dfxengine.entities.definition.ComponentDefinitionImpl;
import me.datafox.dfxengine.entities.definition.EntityDefinitionImpl;
import me.datafox.dfxengine.entities.definition.HandleDefinitionImpl;
import me.datafox.dfxengine.entities.definition.SpaceDefinitionImpl;
import me.datafox.dfxengine.entities.state.ComponentStateImpl;
import me.datafox.dfxengine.entities.state.EngineStateImpl;
import me.datafox.dfxengine.entities.state.EntityStateImpl;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder(toBuilder = true)
public class DefaultSerializationHandlerConfiguration {
    private final OutputType outputType;
    private final boolean prettyPrint;
    @Singular
    private final List<ElementType<?,?>> types;

    @Component(order = Integer.MAX_VALUE)
    public static DefaultSerializationHandlerConfiguration defaultConfiguration() {
        return DefaultSerializationHandlerConfiguration
                .builder()
                .outputType(OutputType.json)
                .prettyPrint(false)
                .type(new ElementType<>(DataPackImpl.class, "entities", EntityDefinitionImpl.class))
                .type(new ElementType<>(DataPackImpl.class, "spaces", SpaceDefinitionImpl.class))
                .type(new ElementType<>(EntityDefinitionImpl.class, "components", ComponentDefinitionImpl.class))
                .type(new ElementType<>(SpaceDefinitionImpl.class, "handles", HandleDefinitionImpl.class))
                .type(new ElementType<>(EngineStateImpl.class, "singleEntities", EntityStateImpl.class))
                .type(new ElementType<>(EngineStateImpl.class, "multiEntities", EntityStateImpl.class))
                .type(new ElementType<>(EntityStateImpl.class, "components", ComponentStateImpl.class))
                .build();
    }

    @Data
    public static class ElementType<O, T> {
        private final Class<O> type;
        private final String fieldName;
        private final Class<T> elementType;
    }
}
