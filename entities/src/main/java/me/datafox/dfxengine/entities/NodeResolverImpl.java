package me.datafox.dfxengine.entities;

import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.NodeResolver;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.event.RunTreesEvent;
import me.datafox.dfxengine.entities.exception.InvalidNodeTreeException;
import me.datafox.dfxengine.entities.api.node.*;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.*;

/**
 * @author datafox
 */
@Component
public class NodeResolverImpl implements NodeResolver {
    @Inject
    private Context context;

    public void run(Collection<? extends NodeTree> trees) {
        trees.stream()
                .sorted()
                .forEach(this::run);
    }

    public void run(NodeTree tree) {
        boolean group = tree.getAttributes().contains(NodeTreeAttribute.GROUP);

        if(group) {
            context.setCurrentGroup((NodeGroup) tree);
        } else {
            context.setCurrentEntity(tree.getComponent().getEntity());
            context.setCurrentComponent(tree.getComponent());
            context.setCurrentTree(tree);
        }
        Map<Node,List<NodeData<?>>> resolved = new HashMap<>();

        for(Node node : tree.getNodes()) {
            context.setCurrentNode(node);

            List<NodeData<?>> list = new ArrayList<>(node.getInputs().size());
            for(NodeInput<?> in : node.getInputs()) {
                NodeOutput<?> out = in.getConnection();
                if(!resolved.containsKey(out)) {
                    throw new InvalidNodeTreeException();
                }
                int i = out.getNode().getOutputs().indexOf(out);
                list.add(resolved.get(out.getNode()).get(i));
            }

            resolved.put(node, node.process(list, context));
        }

        context.setCurrentNode(null);
        if(group) {
            context.setCurrentGroup(null);
        } else {
            context.setCurrentTree(null);
            context.setCurrentComponent(null);
            context.setCurrentEntity(null);
        }
    }

    @EventHandler
    private void handleEvent(RunTreesEvent event) {
        run(event.getTrees());
    }
}
