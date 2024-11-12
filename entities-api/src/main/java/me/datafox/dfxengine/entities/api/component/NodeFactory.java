package me.datafox.dfxengine.entities.api.component;

import me.datafox.dfxengine.entities.api.entity.NodeTreeOwner;
import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;

/**
 * @author datafox
 */
public interface NodeFactory {
    NodeTree buildTree(NodeTreeOwner owner, NodeTreeDefinition definition);
}
