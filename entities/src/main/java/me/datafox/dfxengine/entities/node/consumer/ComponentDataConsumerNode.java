package me.datafox.dfxengine.entities.node.consumer;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.ConsumerNode;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.NodeInputImpl;
import me.datafox.dfxengine.handles.api.Handle;

import java.util.List;

/**
 * @author datafox
 */
@Data
public class ComponentDataConsumerNode<T> implements ConsumerNode {
    private final NodeTree tree;

    private final Handle handle;

    private final List<NodeInput<T>> inputs;

    public ComponentDataConsumerNode(NodeTree tree, DataType<T> type, String handle, Context context) {
        this.tree = tree;
        this.handle = context.getHandles().getDataHandle(handle);
        inputs = List.of(new NodeInputImpl<>(this, type));
    }

    @Override
    public void consume(List<NodeData<?>> inputs, Context context) {
        tree.getComponent().putData(inputs.get(0).toEntityData(handle));
    }
}
