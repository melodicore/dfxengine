package me.datafox.dfxengine.entities.definition.node.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.consumer.ComponentDataConsumerNode;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.entities.utils.SerializationUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentDataConsumerNodeDefinition<T> implements NodeDefinition<ComponentDataConsumerNode<T>> {
    public List<NodeMapping> mappings;

    public DataType<T> type;

    public String handle;

    public boolean stateful;

    @Override
    public ComponentDataConsumerNode<T> build(NodeTree tree, Context context) {
        return new ComponentDataConsumerNode<>(tree, type, handle, stateful, context);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("data", ComponentDataConsumerNodeDefinition.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return SerializationUtils.getNodeMappingsDefaultElement(ComponentDataConsumerNodeDefinition.class);
    }
}

