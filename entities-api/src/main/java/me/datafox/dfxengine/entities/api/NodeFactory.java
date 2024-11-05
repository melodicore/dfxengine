package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;

/**
 * @author datafox
 */
public interface NodeFactory {
    NodeTree buildTree(NodeTreeOwner owner, NodeTreeDefinition definition);
}
