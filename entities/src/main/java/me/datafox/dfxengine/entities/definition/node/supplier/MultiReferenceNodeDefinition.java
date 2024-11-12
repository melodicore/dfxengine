package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.reference.Reference;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.node.supplier.MultiReferenceNode;
import me.datafox.dfxengine.injector.api.annotation.Component;

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

    @Component
    public static ClassTag getTag() {
        return new ClassTag("multiRef", MultiReferenceNodeDefinition.class);
    }
}
