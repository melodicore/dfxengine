package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.node.NodeTree;

import java.util.List;

/**
 * @author datafox
 */
public interface NodeTreeOwner {
    List<NodeTree> getTrees();
}
