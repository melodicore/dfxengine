package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.condition.Condition;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.node.supplier.ConditionNode;
import me.datafox.dfxengine.injector.api.annotation.Component;

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

    @Component
    public static ClassTag getTag() {
        return new ClassTag("condition", ConditionNodeDefinition.class);
    }
}
