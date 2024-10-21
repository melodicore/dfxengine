package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeOutput;

/**
 * @author datafox
 */
@Data
public class NodeInputImpl<T> implements NodeInput<T> {
    private final Node node;

    private final DataType<T> type;

    private NodeOutput<T> connection;

    public NodeInputImpl(Node node, DataType<T> type) {
        this.node = node;
        this.type = type;
    }

    public void connect(NodeOutputImpl<T> output) {
        connection = output;
        output.addConnection(this);
    }
}
