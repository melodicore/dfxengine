package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Condition;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.supplier.ConditionNode;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionNodeDefinition implements SupplierNodeDefinition<ConditionNode> {
    public Condition condition;

    @Override
    public ConditionNode build(NodeTree tree, Context context) {
        return new ConditionNode(tree, condition);
    }
}
