package me.datafox.dfxengine.entities.component;

import com.esotericsoftware.jsonbeans.Json;
import me.datafox.dfxengine.entities.api.component.SerializationHandlerConfiguration;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.entities.serialization.ClassSerializerWrapper;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.List;

/**
 * @author datafox
 */
@Component(order = Integer.MIN_VALUE)
public class DefaultSerializationHandlerConfiguration implements SerializationHandlerConfiguration<Json, Json, DefaultSerializationHandler> {
    private final List<ClassTag> names;

    private final List<DefaultElement> elements;

    private final List<ClassSerializerWrapper<?>> serializers;

    @Inject
    public DefaultSerializationHandlerConfiguration(List<ClassTag> tags, List<DefaultElement> elements, List<ClassSerializerWrapper<?>> serializers) {
        this.names = tags;
        this.elements = elements;
        this.serializers = serializers;
    }

    @Override
    public Json configure(Json builder) {
        names.forEach(tag -> builder.addClassTag(tag.getTag(), tag.getType()));
        elements.forEach(element -> builder.setElementType(element.getType(), element.getFieldName(), element.getElementType()));
        serializers.forEach(serializer -> setSerializer(builder, serializer));
        return builder;
    }

    private <T> void setSerializer(Json builder, ClassSerializerWrapper<T> serializer) {
        builder.setSerializer(serializer.getType(), serializer.getSerializer());
    }
}
