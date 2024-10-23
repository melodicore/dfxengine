package me.datafox.dfxengine.entities.definition.node.group;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.NodeGroupImpl;

import java.util.function.Supplier;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class GroupInputNodeDefinition implements SupplierNodeDefinition<NodeGroupImpl.InputNode> {
    private transient Supplier<NodeGroupImpl.InputNode> input;

    @Override
    public NodeGroupImpl.InputNode build(NodeTree tree, Context context) {
        return input.get();
    }

    public void setConstructor(Supplier<NodeGroupImpl.InputNode> input) {
        this.input = input;
    }
}
