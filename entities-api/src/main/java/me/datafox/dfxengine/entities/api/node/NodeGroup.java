package me.datafox.dfxengine.entities.api.node;

import me.datafox.dfxengine.handles.api.Handle;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author datafox
 */
public interface NodeGroup extends NodeTree, Node {
    Set<NodeTreeAttribute> GROUP_SET = Collections.unmodifiableSet(EnumSet.of(NodeTreeAttribute.GROUP));

    @Override
    default Handle getHandle() {
        return null;
    }

    @Override
    default Set<NodeTreeAttribute> getAttributes() {
        return GROUP_SET;
    }
}
