package me.datafox.dfxengine.entities.component;

import com.esotericsoftware.jsonbeans.Json;
import me.datafox.dfxengine.entities.api.component.SerializationHandlerConfiguration;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.List;

/**
 * @author datafox
 */
@Component
public class DefaultSerializationHandlerConfiguration implements SerializationHandlerConfiguration<Json, Json, DefaultSerializationHandler> {
    private final List<ClassTag> names;

    private final List<DefaultElement> elements;

    @Inject
    public DefaultSerializationHandlerConfiguration(List<ClassTag> tags, List<DefaultElement> elements) {
        this.names = tags;
        this.elements = elements;
    }

    @Override
    public Json configure(Json builder) {
        names.forEach(tag -> builder.addClassTag(tag.getTag(), tag.getType()));
        elements.forEach(element -> builder.setElementType(element.getType(), element.getFieldName(), element.getElementType()));
        return builder;
    }
}
