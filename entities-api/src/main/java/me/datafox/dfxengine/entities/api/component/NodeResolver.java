package me.datafox.dfxengine.entities.api.component;

import me.datafox.dfxengine.entities.api.node.NodeTree;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface NodeResolver {
    void run(Collection<? extends NodeTree> trees);

    void run(Stream<? extends NodeTree> trees);

    void run(NodeTree tree);
}
