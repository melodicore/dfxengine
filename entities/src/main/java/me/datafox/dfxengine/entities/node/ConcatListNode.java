package me.datafox.dfxengine.entities.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
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
public class ConcatListNode<T> extends CachingOutputNode {
    private final NodeTree tree;

    private final List<NodeInput<List<T>>> inputs;

    private final NodeOutput<List<T>> output;

    private final List<NodeOutput<List<T>>> outputs;

    public ConcatListNode(NodeTree tree, ListDataType<T> type, int inputCount) {
        this.tree = tree;
        inputs = IntStream.range(0, inputCount)
                .mapToObj(i -> new NodeInputImpl<>(this, type))
                .collect(Collectors.toUnmodifiableList());
        output = new NodeOutputImpl<>(this, type);
        outputs = List.of(output);
    }

    @Override
    protected List<NodeData<?>> calculateOutputs(List<NodeData<?>> inputs, Context context) {
        return List.of(new NodeDataImpl<>(output.getType(),
                output.getType().cast(inputs
                        .stream()
                        .map(NodeData::getData)
                        .map(o -> (List<?>) o)
                        .flatMap(List::stream)
                        .collect(Collectors.toUnmodifiableList()))));
    }
}
