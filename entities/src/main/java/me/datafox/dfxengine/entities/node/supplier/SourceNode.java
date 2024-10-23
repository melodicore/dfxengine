package me.datafox.dfxengine.entities.node.supplier;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.SupplierNode;
import me.datafox.dfxengine.entities.data.NodeDataImpl;
import me.datafox.dfxengine.entities.node.NodeOutputImpl;

import java.util.List;

/**
 * @author datafox
 */
@Data
public class SourceNode<T> implements SupplierNode {
    private final NodeTree tree;

    private final List<NodeData<?>> dataList;

    private final List<NodeOutput<T>> outputs;

    public SourceNode(NodeTree tree, DataType<T> type, T data) {
        this.tree = tree;
        dataList = List.of(new NodeDataImpl<>(type, data));
        outputs = List.of(new NodeOutputImpl<>(this, type));
    }

    @Override
    public List<NodeData<?>> supply(Context context) {
        return dataList;
    }
}
