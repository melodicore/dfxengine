package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.node.NodeTree;

import java.util.Collection;

/**
 * @author datafox
 */
public interface NodeResolver {
    void run(Collection<? extends NodeTree> trees);

    void run(NodeTree tree);
}
