package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.node.SupplierNode;

import java.util.List;

/**
 * @author datafox
 */
public interface SupplierNodeDefinition<T extends SupplierNode> extends NodeDefinition<T> {
    @Override
    default List<NodeMapping> getMappings() {
        return List.of();
    }
}
