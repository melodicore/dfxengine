package me.datafox.dfxengine.entities.api.event;

import me.datafox.dfxengine.entities.api.node.NodeTree;

import java.util.Collection;

/**
 * @author datafox
 */
@FunctionalInterface
public interface RunTreesEvent {
    Collection<NodeTree> getTrees();

    static RunTreesEvent of(Collection<NodeTree> trees) {
        return () -> trees;
    }
}
