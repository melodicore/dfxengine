package me.datafox.dfxengine.entities.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
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

    private final SingleDataType<Value> type;

    private final List<NodeInput<?>> inputs;

    private final List<NodeOutput<?>> outputs;

    public ValueNode(NodeTree tree, boolean immutable) {
        this.tree = tree;
        this.type = SingleDataTypeImpl.of(Value.class, immutable ? 1 : 0);
        inputs = List.of(new NodeInputImpl<>(this, SingleDataTypeImpl.of(Handle.class)),
                new NodeInputImpl<>(this, SingleDataTypeImpl.of(Numeral.class)));
        outputs = List.of(new NodeOutputImpl<>(this, type));
    }

    @Override
    protected List<NodeData<?>> calculateOutputs(List<NodeData<?>> inputs, Context context) {
        Handle handle = (Handle) inputs.get(0).getData();
        Numeral numeral = (Numeral) inputs.get(1).getData();
        return List.of(new NodeDataImpl<>(type,
                new ValueImpl(handle, numeral,
                        type.getVariation() == 1)));
    }
}
