package me.datafox.dfxengine.entities.api.entity;

import me.datafox.dfxengine.entities.api.node.NodeTree;

import java.util.List;

/**
 * @author datafox
 */
public interface NodeTreeOwner {
    List<NodeTree> getTrees();
}
