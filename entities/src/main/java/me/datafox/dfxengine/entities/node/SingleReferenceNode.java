package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.SupplierNode;
import me.datafox.dfxengine.entities.data.NodeDataImpl;
import me.datafox.dfxengine.handles.api.Handle;

import java.util.List;

/**
 * @author datafox
 */
@Data
public class SingleReferenceNode<T> implements SupplierNode {
    private final NodeTree tree;

    private final NodeOutput<T> output;

    private final List<NodeOutput<T>> outputs;

    private final Handle entity;

    private final Handle component;

    private final Handle data;

    public SingleReferenceNode(NodeTree tree, DataType<T> type, String entity, String component, String data, Context context) {
        this.tree = tree;
        output = new NodeOutputImpl<>(this, type);
        outputs = List.of(output);
        this.entity = context.getHandles().getEntityHandle(entity);
        this.component = context.getHandles().getComponentHandle(component);
        this.data = context.getHandles().getDataHandle(data);
    }

    @Override
    public List<NodeData<?>> supply(Context context) {
        return List.of(new NodeDataImpl<>(output.getType(),
                context.getEngine()
                        .getEntities()
                        .get(entity)
                        .getComponents()
                        .get(component)
                        .getData(output.getType())
                        .get(data)
                        .getData()));
    }
}
