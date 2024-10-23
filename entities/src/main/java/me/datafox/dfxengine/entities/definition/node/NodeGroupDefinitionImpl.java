package me.datafox.dfxengine.entities.definition.node;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeGroupDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeGroup;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.NodeGroupImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class NodeGroupDefinitionImpl implements NodeGroupDefinition {
    public ArrayList<NodeDefinition<?>> nodes;

    public ArrayList<NodeMapping> mappings;

    public ArrayList<DataType<?>> inputs;

    public ArrayList<DataType<?>> outputs;

    @Builder
    public NodeGroupDefinitionImpl(@Singular List<NodeDefinition<?>> nodes,
                                   @Singular List<NodeMapping> mappings,
                                   @Singular List<DataType<?>> inputs,
                                   @Singular List<DataType<?>> outputs) {
        this.nodes = new ArrayList<>(nodes);
        this.mappings = new ArrayList<>(mappings);
        this.inputs = new ArrayList<>(inputs);
        this.outputs = new ArrayList<>(outputs);
    }

    @Override
    public NodeGroup build(NodeTree tree, Context context) {
        return new NodeGroupImpl(context.getCurrentTree(), inputs, outputs, this, context);
    }
}