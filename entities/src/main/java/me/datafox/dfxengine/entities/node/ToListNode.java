package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.NodeDataImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author datafox
 */
@Data
public class ToListNode<T> implements Node {
    private final NodeTree tree;

    private final List<NodeInput<T>> inputs;

    private final NodeOutput<List<T>> output;

    private final List<NodeOutput<List<T>>> outputs;

    public ToListNode(NodeTree tree, SingleDataType<T> type, int inputCount) {
        this.tree = tree;
        inputs = IntStream.range(0, inputCount)
                .mapToObj(i -> new NodeInputImpl<>(this, type))
                .collect(Collectors.toUnmodifiableList());
        output = new NodeOutputImpl<>(this,
                type.toList());
        outputs = List.of(output);
    }

    @Override
    public List<NodeData<?>> process(List<NodeData<?>> inputs, Context context) {
        return List.of(new NodeDataImpl<>(output.getType(),
                output.getType().cast(inputs
                        .stream()
                        .map(NodeData::getData)
                        .collect(Collectors.toUnmodifiableList()))));
    }
}
