package me.datafox.dfxengine.entities.definition.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.node.SourceNode;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.modifier.MappingOperationModifier;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialValueSourceNodeDefinition implements SupplierNodeDefinition<SourceNode<Value>> {
    public int index;

    @Override
    public SourceNode<Value> build(NodeTree tree, Context context) {
        return new SourceNode<>(tree,
                SingleDataTypeImpl.of(Value.class, 3),
                MappingOperationModifier.resultValue(index));
    }
}
