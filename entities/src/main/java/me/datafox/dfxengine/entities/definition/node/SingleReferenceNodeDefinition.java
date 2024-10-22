package me.datafox.dfxengine.entities.definition.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.SingleReferenceNode;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleReferenceNodeDefinition<T> implements SupplierNodeDefinition<SingleReferenceNode<T>> {
    public DataType<T> type;

    public String entity;

    public String component;

    public String data;

    @Override
    public SingleReferenceNode<T> build(NodeTree tree, Context context) {
        return new SingleReferenceNode<>(tree, type, entity, component, data, context);
    }
}
