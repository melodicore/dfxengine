package me.datafox.dfxengine.entities.node.supplier;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Condition;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.entities.api.node.NodeOutput;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.api.node.SupplierNode;
import me.datafox.dfxengine.entities.data.NodeDataImpl;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.node.NodeOutputImpl;

import java.util.List;

/**
 * @author datafox
 */
@Data
public class ConditionNode implements SupplierNode {
    private static final List<NodeData<?>> TRUE = List.of(new NodeDataImpl<>(SingleDataTypeImpl.of(Boolean.class), true));

    private static final List<NodeData<?>> FALSE = List.of(new NodeDataImpl<>(SingleDataTypeImpl.of(Boolean.class), false));

    private final NodeTree tree;

    private final List<NodeOutput<Boolean>> outputs;

    private final Condition condition;

    public ConditionNode(NodeTree tree, Condition condition) {
        this.tree = tree;
        outputs = List.of(new NodeOutputImpl<>(this, SingleDataTypeImpl.of(Boolean.class)));
        this.condition = condition;
    }

    @Override
    public List<NodeData<?>> supply(Context context) {
        return condition.test(context) ? TRUE : FALSE;
    }
}
