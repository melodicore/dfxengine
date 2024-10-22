package me.datafox.dfxengine.entities.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.NodeDataImpl;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.ValueImpl;
import me.datafox.dfxengine.values.api.Value;

import java.util.List;

/**
 * @author datafox
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ValueNode extends CachingOutputNode {
    private final NodeTree tree;

    private final NodeInput<Handle> handle;

    private final NodeInput<Numeral> numeral;

    private final List<NodeInput<?>> inputs;

    private final NodeOutput<Value> output;

    private final List<NodeOutput<Value>> outputs;

    public ValueNode(NodeTree tree, boolean immutable) {
        this.tree = tree;
        handle = new NodeInputImpl<>(this, SingleDataTypeImpl.of(Handle.class));
        numeral = new NodeInputImpl<>(this, SingleDataTypeImpl.of(Numeral.class));
        inputs = List.of(handle, numeral);
        output = new NodeOutputImpl<>(this, SingleDataTypeImpl.of(Value.class, immutable ? 1 : 0));
        outputs = List.of(output);
    }

    @Override
    protected List<NodeData<?>> calculateOutputs(List<NodeData<?>> inputs, Context context) {
        Handle handle = (Handle) inputs.get(0).getData();
        Numeral numeral = (Numeral) inputs.get(1).getData();
        return List.of(new NodeDataImpl<>(output.getType(),
                new ValueImpl(handle, numeral,
                        output.getType().getVariation() == 1)));
    }
}
