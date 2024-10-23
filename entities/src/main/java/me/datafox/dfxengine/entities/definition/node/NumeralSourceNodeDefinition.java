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
import me.datafox.dfxengine.entities.utils.NumeralUtils;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NumeralSourceNodeDefinition implements SupplierNodeDefinition<SourceNode<Numeral>> {
    public NumeralType type;

    public String value;

    @Override
    public SourceNode<Numeral> build(NodeTree tree, Context context) {
        return new SourceNode<>(tree,
                SingleDataTypeImpl.of(Numeral.class),
                NumeralUtils.getNumeral(type, value));
    }
}