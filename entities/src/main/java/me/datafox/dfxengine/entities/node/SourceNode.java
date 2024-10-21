package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.SupplierNode;
import me.datafox.dfxengine.entities.data.NodeDataImpl;

import java.util.List;

/**
 * @author datafox
 */
@Data
public class SourceNode<T> implements SupplierNode {
    private final NodeTree tree;

    private final NodeData<T> data;

    private final List<NodeData<?>> dataList;

    private final NodeOutput<T> output;

    private final List<NodeOutput<T>> outputs;

    public SourceNode(NodeTree tree, DataType<T> type, T data) {
        this.tree = tree;
        this.data = new NodeDataImpl<>(type, data);
        dataList = List.of(this.data);
        output = new NodeOutputImpl<>(this, type);
        outputs = List.of(output);
    }

    @Override
    public List<NodeData<?>> supply(Context context) {
        return dataList;
    }
}
