package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.node.NodeGroup;
import me.datafox.dfxengine.entities.api.node.NodeTreeAttribute;

import java.util.List;

/**
 * @author datafox
 */
public interface NodeGroupDefinition extends NodeTreeDefinition, NodeDefinition<NodeGroup> {
    List<NodeTreeAttribute> GROUP_LIST = List.of(NodeTreeAttribute.GROUP);

    @Override
    default String getHandle() {
        return null;
    }

    @Override
    default int getOrder() {
        return 0;
    }

    @Override
    default List<NodeTreeAttribute> getAttributes() {
        return GROUP_LIST;
    }
}
