package me.datafox.dfxengine.entities.node.consumer;

import lombok.Data;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.entity.EntityComponent;
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

    private final List<NodeInput<?>> inputs;

    private final Handle handle;

    private final boolean stateful;

    public ComponentDataConsumerNode(NodeTree tree, DataType<T> type, String handle, boolean stateful, Context context) {
        this.tree = tree;
        inputs = List.of(new NodeInputImpl<>(this, type));
        this.handle = context.getHandles().getDataHandle(handle);
        this.stateful = stateful;
    }

    @Override
    public void consume(List<NodeData<?>> inputs, Context context) {
        ((EntityComponent) tree.getOwner()).putData(inputs.get(0).toEntityData(handle, stateful));
    }
}
