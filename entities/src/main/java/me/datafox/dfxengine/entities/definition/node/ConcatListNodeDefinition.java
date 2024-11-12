package me.datafox.dfxengine.entities.definition.node;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.ListDataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.data.ListDataTypeImpl;
import me.datafox.dfxengine.entities.node.ConcatListNode;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.entities.utils.SerializationUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;

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

    @Component
    public static ClassTag getTag() {
        return new ClassTag("concatList", ConcatListNodeDefinition.class);
    }

    @Component
    public static List<DefaultElement> getDefaultElements() {
        return List.of(SerializationUtils.getNodeMappingsDefaultElement(ConcatListNodeDefinition.class),
                new DefaultElement(ConcatListNodeDefinition.class, "type", ListDataTypeImpl.class));
    }
}
