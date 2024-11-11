package me.datafox.dfxengine.entities.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
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
@EqualsAndHashCode(callSuper = false)
public class ToListNode<T> extends CachingOutputNode {
    private final NodeTree tree;

    private final SingleDataType<T> type;

    private final ListDataType<T> listType;

    private final List<NodeInput<?>> inputs;

    private final List<NodeOutput<?>> outputs;

    public ToListNode(NodeTree tree, SingleDataType<T> type, int inputCount) {
        this.tree = tree;
        this.type = type;
        listType = type.toList();
        inputs = IntStream.range(0, inputCount)
                .mapToObj(i -> new NodeInputImpl<>(this, type))
                .collect(Collectors.toUnmodifiableList());
        outputs = List.of(new NodeOutputImpl<>(this, listType));
    }

    @Override
    protected List<NodeData<?>> calculateOutputs(List<NodeData<?>> inputs, Context context) {
        return List.of(new NodeDataImpl<>(listType,
                listType.cast(inputs
                        .stream()
                        .map(NodeData::getData)
                        .collect(Collectors.toUnmodifiableList()))));
    }
}
