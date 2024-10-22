package me.datafox.dfxengine.entities.definition.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.MultiListReferenceNode;
import me.datafox.dfxengine.entities.node.MultiReferenceNode;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiListReferenceNodeDefinition<T> implements SupplierNodeDefinition<MultiListReferenceNode<T>> {
    public ListDataType<T> type;

    public Reference entity;

    public Reference component;

    public Reference data;

    @Override
    public MultiListReferenceNode<T> build(NodeTree tree, Context context) {
        return new MultiListReferenceNode<>(tree, type, entity, component, data);
    }
}
