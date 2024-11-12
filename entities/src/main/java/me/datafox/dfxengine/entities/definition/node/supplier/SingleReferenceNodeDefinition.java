package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.node.supplier.SingleReferenceNode;
import me.datafox.dfxengine.injector.api.annotation.Component;

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

    @Component
    public static ClassTag getTag() {
        return new ClassTag("ref", SingleReferenceNodeDefinition.class);
    }
}
