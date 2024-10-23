package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.supplier.MultiReferenceNode;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiReferenceNodeDefinition<T> implements SupplierNodeDefinition<MultiReferenceNode<T>> {
    public SingleDataType<T> type;

    public Reference entity;

    public Reference component;

    public Reference data;

    @Override
    public MultiReferenceNode<T> build(NodeTree tree, Context context) {
        return new MultiReferenceNode<>(tree, type, entity, component, data);
    }
}
