package me.datafox.dfxengine.entities.definition.node.supplier.operation;

import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.node.supplier.SourceNode;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;

/**
 * @author datafox
 */
public class SqrtOperationSourceNodeDefinition implements SupplierNodeDefinition<SourceNode<Operation>> {
    @Override
    public SourceNode<Operation> build(NodeTree tree, Context context) {
        return new SourceNode<>(tree, SingleDataTypeImpl.of(Operation.class),
                (SourceOperation) Operations::sqrt);
    }
}
