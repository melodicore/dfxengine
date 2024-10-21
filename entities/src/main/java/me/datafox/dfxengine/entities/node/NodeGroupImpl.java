package me.datafox.dfxengine.entities.node;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;
import me.datafox.dfxengine.entities.api.event.RunTreeEvent;
import me.datafox.dfxengine.entities.api.node.*;
import me.datafox.dfxengine.entities.definition.node.GroupInputNodeDefinition;
import me.datafox.dfxengine.entities.definition.node.GroupOutputNodeDefinition;
import me.datafox.dfxengine.entities.exception.InvalidNodeException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
public class NodeGroupImpl implements NodeGroup {
    private final NodeTree tree;

    private final NodeTree delegate;

    private final List<NodeInput<?>> inputs;

    private final List<NodeOutput<?>> outputs;

    private List<DataType<?>> inputTypes;

    private List<DataType<?>> outputTypes;

    private List<NodeData<?>> inputData;

    private List<NodeData<?>> outputData;

    public NodeGroupImpl(NodeTree tree, List<DataType<?>> inputs, List<DataType<?>> outputs, NodeTreeDefinition definition, Context context) {
        this.tree = tree;
        inputTypes = inputs;
        this.inputs = inputs
                .stream()
                .map(type -> new NodeInputImpl<>(this, type))
                .collect(Collectors.toUnmodifiableList());
        outputTypes = outputs;
        this.outputs = outputs
                .stream()
                .map(type -> new NodeOutputImpl<>(this, type))
                .collect(Collectors.toUnmodifiableList());
        NodeDefinition<?> inputDefinition = definition.getNodes().get(0);
        NodeDefinition<?> outputDefinition = definition.getNodes().get(definition.getNodes().size() - 1);
        if(!(inputDefinition instanceof GroupInputNodeDefinition)) {
            throw new InvalidNodeException("First node of group is not group input node");
        }
        if(!(outputDefinition instanceof GroupOutputNodeDefinition)) {
            throw new InvalidNodeException("Last node of group is not group output node");
        }
        ((GroupInputNodeDefinition) inputDefinition).setConstructor(this::createInput);
        ((GroupOutputNodeDefinition) outputDefinition).setConstructor(this::createOutput);
        delegate = context.getNodeFactory().buildTree(tree.getComponent(), definition);
        inputTypes = null;
        outputTypes = null;
    }

    @Override
    public EntityComponent getComponent() {
        return delegate.getComponent();
    }

    @Override
    public int getOrder() {
        return delegate.getOrder();
    }

    @Override
    public List<Node> getNodes() {
        return delegate.getNodes();
    }

    @Override
    public List<NodeData<?>> process(List<NodeData<?>> inputs, Context context) {
        inputData = inputs;
        context.invokeEvent(RunTreeEvent.of(this));
        return outputData;
    }

    private InputNode createInput() {
        return new InputNode(inputTypes);
    }

    private OutputNode createOutput() {
        return new OutputNode(outputTypes);
    }

    @Data
    public class InputNode implements SupplierNode {
        private final List<NodeOutput<?>> outputs;

        private InputNode(List<DataType<?>> types) {
            outputs = types
                    .stream()
                    .map(type -> new NodeOutputImpl<>(this, type))
                    .collect(Collectors.toUnmodifiableList());
        }

        @Override
        public List<NodeData<?>> supply(Context context) {
            return inputData;
        }

        @Override
        public NodeTree getTree() {
            return NodeGroupImpl.this;
        }
    }

    @Data
    public class OutputNode implements ConsumerNode {
        private final List<NodeInput<?>> inputs;

        private OutputNode(List<DataType<?>> types) {
            inputs = types
                    .stream()
                    .map(type -> new NodeInputImpl<>(this, type))
                    .collect(Collectors.toUnmodifiableList());
        }

        @Override
        public void consume(List<NodeData<?>> inputs, Context context) {
            outputData = inputs;
        }

        @Override
        public NodeTree getTree() {
            return NodeGroupImpl.this;
        }
    }
}
