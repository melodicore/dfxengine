package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.node.NodeTreeAttribute;

import java.util.List;

/**
 * @author datafox
 */
public interface NodeTreeDefinition {
    String getHandle();

    int getOrder();

    List<NodeDefinition<?>> getNodes();

    List<NodeTreeAttribute> getAttributes();
}
