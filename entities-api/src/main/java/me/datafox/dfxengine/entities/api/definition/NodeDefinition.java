package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.node.Node;
import me.datafox.dfxengine.entities.api.node.NodeTree;

import java.util.List;

/**
 * @author datafox
 */
public interface NodeDefinition<T extends Node> {
    List<NodeMapping> getMappings();

    T build(NodeTree tree, Context context);
}
