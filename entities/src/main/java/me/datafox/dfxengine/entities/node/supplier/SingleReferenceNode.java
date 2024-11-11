package me.datafox.dfxengine.entities.node.supplier;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.SupplierNode;
import me.datafox.dfxengine.entities.exception.InvalidNodeException;
import me.datafox.dfxengine.entities.node.NodeOutputImpl;
import me.datafox.dfxengine.handles.api.Handle;

import java.util.List;

/**
 * @author datafox
 */
@Data
public class SingleReferenceNode<T> implements SupplierNode {
    private final NodeTree tree;

    private final List<NodeOutput<?>> outputs;

    private final Handle entity;

    private final Handle component;

    private final Handle data;

    public SingleReferenceNode(NodeTree tree, DataType<T> type, String entity, String component, String data, Context context) {
        this.tree = tree;
        outputs = List.of(new NodeOutputImpl<>(this, type));
        this.entity = context.getHandles().getEntityHandle(entity);
        if(this.entity.getTags().contains(context.getHandles().getMultiEntityTag())) {
            throw new InvalidNodeException();
        }
        this.component = context.getHandles().getComponentHandle(component);
        this.data = context.getHandles().getDataHandle(data);
    }

    @Override
    public List<NodeData<?>> supply(Context context) {
        return List.of((NodeData<?>) outputs.get(0).getType(),
                context.getEngine()
                        .getEntity(entity)
                        .getComponents()
                        .get(component)
                        .getData(outputs.get(0).getType())
                        .get(data)
                        .toNodeData());
    }
}
