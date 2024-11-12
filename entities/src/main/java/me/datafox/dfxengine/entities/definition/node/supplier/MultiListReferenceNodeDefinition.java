package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.reference.Reference;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.node.supplier.MultiListReferenceNode;
import me.datafox.dfxengine.injector.api.annotation.Component;

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

    @Component
    public static ClassTag getTag() {
        return new ClassTag("multiListRef", MultiListReferenceNodeDefinition.class);
    }
}
