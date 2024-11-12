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
import me.datafox.dfxengine.entities.utils.NumeralUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaticValueSourceNodeDefinition implements SupplierNodeDefinition<SourceNode<Value>> {
    public NumeralType type;

    public String value;

    @Override
    public SourceNode<Value> build(NodeTree tree, Context context) {
        return new SourceNode<>(tree,
                SingleDataTypeImpl.of(Value.class, 2),
                new StaticValue(NumeralUtils.getNumeral(type, value)));
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("staticValueSource", StaticValueSourceNodeDefinition.class);
    }
}
