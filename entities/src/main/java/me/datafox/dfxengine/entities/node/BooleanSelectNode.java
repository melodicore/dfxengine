package me.datafox.dfxengine.entities.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;

import java.util.List;

/**
 * @author datafox
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BooleanSelectNode<T> extends CachingOutputNode {
    private final NodeTree tree;

    private final List<NodeInput<?>> inputs;

    private final List<NodeOutput<T>> outputs;

    public BooleanSelectNode(NodeTree tree, DataType<T> type) {
        this.tree = tree;
        inputs = List.of(new NodeInputImpl<>(this, type),
                new NodeInputImpl<>(this, type),
                new NodeInputImpl<>(this, SingleDataTypeImpl.of(Boolean.class)));
        outputs = List.of(new NodeOutputImpl<>(this, type));
    }

    @Override
    protected List<NodeData<?>> calculateOutputs(List<NodeData<?>> inputs, Context context) {
        if((Boolean) inputs.get(2).getData()) {
            return List.of(inputs.get(0));
        }
        return List.of(inputs.get(1));
    }
}
