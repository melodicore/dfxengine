package me.datafox.dfxengine.entities.serialization;

import com.esotericsoftware.jsonbeans.JsonSerializer;
import com.esotericsoftware.jsonbeans.OutputType;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import me.datafox.dfxengine.entities.DataPackImpl;
import me.datafox.dfxengine.entities.definition.*;
import me.datafox.dfxengine.entities.definition.action.CreateEntityActionDefinition;
import me.datafox.dfxengine.entities.definition.action.RemoveEntityActionDefinition;
import me.datafox.dfxengine.entities.definition.action.ValueMapOperationActionDefinition;
import me.datafox.dfxengine.entities.definition.action.ValueOperationActionDefinition;
import me.datafox.dfxengine.entities.definition.data.ImmutableValueDataDefinition;
import me.datafox.dfxengine.entities.definition.data.ImmutableValueMapDataDefinition;
import me.datafox.dfxengine.entities.definition.data.ValueDataDefinition;
import me.datafox.dfxengine.entities.definition.data.ValueMapDataDefinition;
import me.datafox.dfxengine.entities.definition.link.ValueMapModifierLinkDefinition;
import me.datafox.dfxengine.entities.definition.link.ValueModifierLinkDefinition;
import me.datafox.dfxengine.entities.definition.modifier.OperationModifierDefinition;
import me.datafox.dfxengine.entities.definition.operation.*;
import me.datafox.dfxengine.entities.definition.system.ValueMapOperationSystemDefinition;
import me.datafox.dfxengine.entities.reference.SelectorComponentReference;
import me.datafox.dfxengine.entities.reference.SelectorEntityReference;
import me.datafox.dfxengine.entities.reference.SelfComponentReference;
import me.datafox.dfxengine.entities.reference.SelfEntityReference;
import me.datafox.dfxengine.entities.reference.data.*;
import me.datafox.dfxengine.entities.reference.selector.*;
import me.datafox.dfxengine.entities.state.*;
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
    @Singular
    private final List<ClassTag<?>> classTags;
    @Singular
    private final List<Serializer<?>> serializers;

    @Component(order = Integer.MAX_VALUE)
    public static DefaultSerializationHandlerConfiguration defaultConfiguration() {
        return DefaultSerializationHandlerConfiguration
                .builder()
                .outputType(OutputType.json)
                .prettyPrint(false)
                //Common types
                .type(new ElementType<>(DataPackImpl.class, "entities", EntityDefinitionImpl.class))
                .type(new ElementType<>(DataPackImpl.class, "spaces", SpaceDefinitionImpl.class))
                .type(new ElementType<>(EntityDefinitionImpl.class, "components", ComponentDefinitionImpl.class))
                .type(new ElementType<>(SpaceDefinitionImpl.class, "handles", HandleDefinitionImpl.class))
                .type(new ElementType<>(EngineStateImpl.class, "singleEntities", EntityStateImpl.class))
                .type(new ElementType<>(EngineStateImpl.class, "multiEntities", EntityStateImpl.class))
                .type(new ElementType<>(EntityStateImpl.class, "components", ComponentStateImpl.class))
                .type(new ElementType<>(ValueMapOperationSystemDefinition.class, "context", MathContextDefinitionImpl.class))
                //Actions
                .classTag(new ClassTag<>("createEntityAction", CreateEntityActionDefinition.class))
                .classTag(new ClassTag<>("removeEntityAction", RemoveEntityActionDefinition.class))
                .classTag(new ClassTag<>("valueMapOperationAction", ValueMapOperationActionDefinition.class))
                .classTag(new ClassTag<>("valueOperationAction", ValueOperationActionDefinition.class))
                //Data
                .classTag(new ClassTag<>("immutableValue", ImmutableValueDataDefinition.class))
                .classTag(new ClassTag<>("immutableValueMap", ImmutableValueMapDataDefinition.class))
                .classTag(new ClassTag<>("value", ValueDataDefinition.class))
                .classTag(new ClassTag<>("valueMap", ValueMapDataDefinition.class))
                //Link
                .classTag(new ClassTag<>("valueMapModifier", ValueMapModifierLinkDefinition.class))
                .classTag(new ClassTag<>("valueModifier", ValueModifierLinkDefinition.class))
                //Modifier
                .classTag(new ClassTag<>("operationModifier", OperationModifierDefinition.class))
                //Operation
                .classTag(new ClassTag<>("add", AddOperationDefinition.class))
                .classTag(new ClassTag<>("cbrt", CbrtOperationDefinition.class))
                .classTag(new ClassTag<>("divide", DivideOperationDefinition.class))
                .classTag(new ClassTag<>("divideReversed", DivideReversedOperationDefinition.class))
                .classTag(new ClassTag<>("exp", ExpOperationDefinition.class))
                .classTag(new ClassTag<>("inverse", InverseOperationDefinition.class))
                .classTag(new ClassTag<>("lerp", LerpOperationDefinition.class))
                .classTag(new ClassTag<>("log2", Log2OperationDefinition.class))
                .classTag(new ClassTag<>("log10", Log10OperationDefinition.class))
                .classTag(new ClassTag<>("logN", LogNOperationDefinition.class))
                .classTag(new ClassTag<>("logNReversed", LogNReversedOperationDefinition.class))
                .classTag(new ClassTag<>("log", LogOperationDefinition.class))
                .classTag(new ClassTag<>("mappingOperationChain", MappingOperationChainDefinition.class))
                .classTag(new ClassTag<>("max", MaxOperationDefinition.class))
                .classTag(new ClassTag<>("min", MinOperationDefinition.class))
                .classTag(new ClassTag<>("multiply", MultiplyOperationDefinition.class))
                .classTag(new ClassTag<>("operationChain", OperationChainDefinition.class))
                .classTag(new ClassTag<>("power", PowerOperationDefinition.class))
                .classTag(new ClassTag<>("powerReversed", PowerReversedOperationDefinition.class))
                .classTag(new ClassTag<>("root", RootOperationDefinition.class))
                .classTag(new ClassTag<>("rootReversed", RootReversedOperationDefinition.class))
                .classTag(new ClassTag<>("sqrt", SqrtOperationDefinition.class))
                .classTag(new ClassTag<>("subtract", SubtractOperationDefinition.class))
                .classTag(new ClassTag<>("subtractReversed", SubtractReversedOperationDefinition.class))
                //System
                .classTag(new ClassTag<>("valueMapOperationSystem", ValueMapOperationSystemDefinition.class))
                //Reference
                .classTag(new ClassTag<>("selectorComponent", SelectorComponentReference.class))
                .classTag(new ClassTag<>("selectorEntity", SelectorEntityReference.class))
                .classTag(new ClassTag<>("selfComponent", SelfComponentReference.class))
                .classTag(new ClassTag<>("selfEntity", SelfEntityReference.class))
                .classTag(new ClassTag<>("selectorValue", SelectorValueDataReference.class))
                .classTag(new ClassTag<>("selectorValueMap", SelectorValueMapDataReference.class))
                .classTag(new ClassTag<>("selectorMapValue", SelectorValueMapValueDataReference.class))
                .classTag(new ClassTag<>("specialValue", SpecialValueDataReference.class))
                .classTag(new ClassTag<>("staticValue", StaticValueDataReference.class))
                //Selector
                .classTag(new ClassTag<>("and", AndSelector.class))
                .classTag(new ClassTag<>("every", EverySelector.class))
                .classTag(new ClassTag<>("group", GroupSelector.class))
                .classTag(new ClassTag<>("handle", HandleSelector.class))
                .classTag(new ClassTag<>("multiHandle", MultiHandleSelector.class))
                .classTag(new ClassTag<>("multiTag", MultiTagSelector.class))
                .classTag(new ClassTag<>("or", OrSelector.class))
                .classTag(new ClassTag<>("tag", TagSelector.class))
                //State
                .classTag(new ClassTag<>("valueMapState", ValueMapState.class))
                .classTag(new ClassTag<>("valueState", ValueState.class))
                .build();
    }

    @Data
    public static class ElementType<T, E> {
        private final Class<T> type;
        private final String fieldName;
        private final Class<E> elementType;
    }

    @Data
    public static class ClassTag<T> {
        private final String tag;
        private final Class<T> type;
    }

    @Data
    public static class Serializer<T> {
        private final Class<T> type;
        private final JsonSerializer<T> serializer;
    }
}
