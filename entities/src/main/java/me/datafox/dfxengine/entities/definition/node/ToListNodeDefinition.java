package me.datafox.dfxengine.entities.definition.node;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.ToListNode;
import me.datafox.dfxengine.entities.node.ValueNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ToListNodeDefinition<T> implements NodeDefinition<ToListNode<T>> {
    public ArrayList<NodeMapping> mappings;

    public SingleDataType<T> type;

    @Builder
    public ToListNodeDefinition(@Singular List<NodeMapping> mappings, SingleDataType<T> type) {
        this.mappings = new ArrayList<>(mappings);
        this.type = type;
    }

    @Override
    public ToListNode<T> build(NodeTree tree, Context context) {
        return new ToListNode<>(tree, type, mappings.size());
    }
}
