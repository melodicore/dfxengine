package me.datafox.dfxengine.entities.definition.node;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.data.SingleDataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.ConcatListNode;
import me.datafox.dfxengine.entities.node.ToListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ConcatListNodeDefinition<T> implements NodeDefinition<ConcatListNode<T>> {
    public ArrayList<NodeMapping> mappings;

    public ListDataType<T> type;

    @Builder
    public ConcatListNodeDefinition(@Singular List<NodeMapping> mappings, ListDataType<T> type) {
        this.mappings = new ArrayList<>(mappings);
        this.type = type;
    }

    @Override
    public ConcatListNode<T> build(NodeTree tree, Context context) {
        return new ConcatListNode<>(tree, type, mappings.size());
    }
}
