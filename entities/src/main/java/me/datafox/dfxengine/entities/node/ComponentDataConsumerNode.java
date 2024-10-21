package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.ConsumerNode;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.handles.api.Handle;

import java.util.List;

/**
 * @author datafox
 */
@Data
public class ComponentDataConsumerNode<T> implements ConsumerNode {
    private final NodeTree tree;

    private final Handle handle;

    private final NodeInput<T> input;

    private final List<NodeInput<?>> inputs;

    public ComponentDataConsumerNode(NodeTree tree, DataType<T> type, String handle, Context context) {
        this.tree = tree;
        this.handle = context.getHandles().getDataHandle(handle);
        input = new NodeInputImpl<>(this, type);
        inputs = List.of(input);
    }

    @Override
    public void consume(List<NodeData<?>> inputs, Context context) {
        T data = input.getType().cast(inputs.get(0).getData());
        tree.getComponent().putData(handle, input.getType(), data);
    }
}
