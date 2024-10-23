package me.datafox.dfxengine.entities.definition.node.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.definition.SupplierNodeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.SingleDataTypeImpl;
import me.datafox.dfxengine.entities.node.supplier.SourceNode;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HandleSourceNodeDefinition implements SupplierNodeDefinition<SourceNode<Handle>> {
    public String space;

    public String handle;

    @Override
    public SourceNode<Handle> build(NodeTree tree, Context context) {
        return new SourceNode<>(tree,
                SingleDataTypeImpl.of(Handle.class),
                context.getHandleManager()
                        .getOrCreateSpace(space)
                        .getOrCreateHandle(handle));
    }
}
