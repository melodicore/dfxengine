package me.datafox.dfxengine.entities.definition.node.supplier.operation;

import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.node.supplier.SourceNode;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;

/**
 * @author datafox
 */
public class LerpOperationSourceNodeDefinition implements SupplierNodeDefinition<SourceNode<Operation>> {
    @Override
    public SourceNode<Operation> build(NodeTree tree, Context context) {
        return new SourceNode<>(tree, SingleDataTypeImpl.of(Operation.class),
                (DualParameterOperation) Operations::lerp);
    }
}
