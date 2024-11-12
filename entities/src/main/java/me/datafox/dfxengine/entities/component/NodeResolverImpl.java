package me.datafox.dfxengine.entities.component;

import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.entity.EntityComponent;
import me.datafox.dfxengine.entities.api.entity.EntitySystem;
import me.datafox.dfxengine.entities.api.component.NodeResolver;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.event.RunTreesEvent;
import me.datafox.dfxengine.entities.exception.InvalidNodeTreeException;
import me.datafox.dfxengine.entities.api.node.*;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class NodeResolverImpl implements NodeResolver {
    @Inject
    private Context context;

    public void run(Collection<? extends NodeTree> trees) {
        run(trees.stream());
    }

    @Override
    public void run(Stream<? extends NodeTree> trees) {
        trees.sorted().forEach(this::run);
    }

    public void run(NodeTree tree) {
        boolean group = tree.getAttributes().contains(NodeTreeAttribute.GROUP);
        boolean system = tree.getAttributes().contains(NodeTreeAttribute.SYSTEM);

        if(group) {
            context.setCurrentGroup((NodeGroup) tree);
        } else if(system) {
            EntitySystem owner = (EntitySystem) tree.getOwner();
            context.setCurrentSystem(owner);
            context.setCurrentTree(tree);
        } else {
            EntityComponent owner = (EntityComponent) tree.getOwner();
            context.setCurrentEntity(owner.getEntity());
            context.setCurrentComponent(owner);
            context.setCurrentTree(tree);
        }
        Map<Node,List<NodeData<?>>> resolved = new HashMap<>();

        for(Node node : tree.getNodes()) {
            context.setCurrentNode(node);

            List<NodeData<?>> list = new ArrayList<>(node.getInputs().size());
            for(NodeInput<?> in : node.getInputs()) {
                NodeOutput<?> out = in.getConnection();
                if(!resolved.containsKey(out.getNode())) {
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
        } else if(system) {
            context.setCurrentTree(null);
            context.setCurrentSystem(null);
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
