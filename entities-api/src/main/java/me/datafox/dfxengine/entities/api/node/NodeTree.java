package me.datafox.dfxengine.entities.api.node;

import me.datafox.dfxengine.entities.api.entity.NodeTreeOwner;
import me.datafox.dfxengine.handles.api.Handle;

import java.util.List;
import java.util.Set;

/**
 * @author datafox
 */
public interface NodeTree extends Comparable<NodeTree> {
    Handle getHandle();

    NodeTreeOwner getOwner();

    int getOrder();

    List<Node> getNodes();

    Set<NodeTreeAttribute> getAttributes();

    @Override
    default int compareTo(NodeTree o) {
        return Integer.compare(getOrder(), o.getOrder());
    }
}
