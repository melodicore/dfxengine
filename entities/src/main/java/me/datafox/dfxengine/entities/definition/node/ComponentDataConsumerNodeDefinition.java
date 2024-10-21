package me.datafox.dfxengine.entities.definition.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.ComponentDataConsumerNode;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentDataConsumerNodeDefinition<T> implements NodeDefinition<ComponentDataConsumerNode<T>> {
    public NodeMapping mapping;

    public DataType<T> type;

    public String handle;

    @Override
    public List<NodeMapping> getMappings() {
        return List.of(mapping);
    }

    @Override
    public ComponentDataConsumerNode<T> build(NodeTree tree, Context context) {
        return new ComponentDataConsumerNode<>(tree, type, handle, context);
    }
}

