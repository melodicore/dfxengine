package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author datafox
 */
@Data
public class NodeOutputImpl<T> implements NodeOutput<T> {
    private final Node node;

    private final DataType<T> type;

    private final List<NodeInput<T>> connections;

    private final List<NodeInput<T>> connectionsInternal;

    public NodeOutputImpl(Node node, DataType<T> type) {
        this.node = node;
        this.type = type;
        connectionsInternal = new ArrayList<>();
        connections = Collections.unmodifiableList(connectionsInternal);
    }

    public void addConnection(NodeInputImpl<T> input) {
        connectionsInternal.add(input);
    }
}
