package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.node.supplier.SourceNode;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.operation.MappingOperationChain;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialNumeralSourceNodeDefinition implements SupplierNodeDefinition<SourceNode<Numeral>> {
    public int index;

    @Override
    public SourceNode<Numeral> build(NodeTree tree, Context context) {
        return new SourceNode<>(tree,
                SingleDataTypeImpl.of(Numeral.class, 1),
                MappingOperationChain.resultNumeral(index));
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("specialNumeralSource", SpecialNumeralSourceNodeDefinition.class);
    }
}
