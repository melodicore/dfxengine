package me.datafox.dfxengine.entities.api.event;

import me.datafox.dfxengine.entities.api.node.NodeTree;

import java.util.Collection;
import java.util.List;

/**
 * @author datafox
 */
@FunctionalInterface
public interface RunTreeEvent extends RunTreesEvent {
    NodeTree getTree();

    @Override
    default Collection<NodeTree> getTrees() {
        return List.of(getTree());
    }

    static RunTreeEvent of(NodeTree tree) {
        return () -> tree;
    }
}
