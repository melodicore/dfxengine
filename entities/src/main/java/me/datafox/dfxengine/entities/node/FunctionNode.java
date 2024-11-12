package me.datafox.dfxengine.entities.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.NodeDataImpl;

import java.util.List;
import java.util.function.Function;

/**
 * @author datafox
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FunctionNode<T, R> extends CachingOutputNode {
    private final NodeTree tree;

    private final DataType<T> inputType;

    private final DataType<R> outputType;

    private final List<NodeInput<?>> inputs;

    private final List<NodeOutput<?>> outputs;

    private final Function<T, R> function;

    public FunctionNode(NodeTree tree, DataType<T> inputType, DataType<R> outputType, Function<T,R> function) {
        this.tree = tree;
        this.inputType = inputType;
        this.outputType = outputType;
        this.inputs = List.of(new NodeInputImpl<>(this, inputType));
        this.outputs = List.of(new NodeOutputImpl<>(this, outputType));
        this.function = function;
    }

    @Override
    protected List<NodeData<?>> calculateOutputs(List<NodeData<?>> inputs, Context context) {
        return List.of(new NodeDataImpl<>(outputType,
                function.apply(inputType.cast(inputs.get(0).getData()))));
    }
}
