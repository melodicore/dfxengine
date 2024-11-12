package me.datafox.dfxengine.entities.component;

import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.component.NodeFactory;
import me.datafox.dfxengine.entities.api.entity.NodeTreeOwner;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeInput;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.exception.InvalidNodeTreeException;
import me.datafox.dfxengine.entities.node.NodeInputImpl;
import me.datafox.dfxengine.entities.node.NodeOutputImpl;
import me.datafox.dfxengine.entities.node.NodeTreeImpl;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class NodeFactoryImpl implements NodeFactory {
    @Inject
    private Context context;

    public NodeTree buildTree(NodeTreeOwner owner, NodeTreeDefinition definition) {
        NodeTreeImpl tree = new NodeTreeImpl(
                definition.getHandle(), owner, definition.getOrder(), definition.getAttributes(), context);

        definition.getNodes()
                .stream()
                .map(def -> def.build(tree, context))
                .forEach(tree::addNode);

        for(int nodeIndex = 0; nodeIndex < definition.getNodes().size(); nodeIndex++) {
            NodeDefinition<?> nodeDefinition = definition.getNodes().get(nodeIndex);
            Node node = tree.getNodes().get(nodeIndex);
            for(int inputIndex = 0; inputIndex < nodeDefinition.getMappings().size(); inputIndex++) {
                NodeMapping mapping = nodeDefinition.getMappings().get(inputIndex);
                NodeInput<?> input = node.getInputs().get(inputIndex);
                NodeOutput<?> output = tree
                        .getNodes()
                        .get(mapping.getTargetNode())
                        .getOutputs()
                        .get(mapping.getTargetOutput());
                castAndConnect(input, output);
            }
        }

        return tree;
    }

    @SuppressWarnings("unchecked")
    private <T> void castAndConnect(NodeInput<T> input, NodeOutput<?> output) {
        if(!input.getType().equals(output.getType())) {
            throw new InvalidNodeTreeException("type mismatch");
        }
        ((NodeInputImpl<T>) input).connect((NodeOutputImpl<T>) output);
    }
}
