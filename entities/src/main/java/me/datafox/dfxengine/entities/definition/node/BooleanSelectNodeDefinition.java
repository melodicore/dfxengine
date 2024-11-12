package me.datafox.dfxengine.entities.definition.node;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.BooleanSelectNode;
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
public class BooleanSelectNodeDefinition<T> implements NodeDefinition<BooleanSelectNode<T>> {
    public ArrayList<NodeMapping> mappings;

    public DataType<T> type;

    @Builder
    public BooleanSelectNodeDefinition(@Singular List<NodeMapping> mappings, DataType<T> type) {
        this.mappings = new ArrayList<>(mappings);
        this.type = type;
    }

    @Override
    public BooleanSelectNode<T> build(NodeTree tree, Context context) {
        return new BooleanSelectNode<>(tree, type);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("boolSelect", BooleanSelectNodeDefinition.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return SerializationUtils.getNodeMappingsDefaultElement(BooleanSelectNode.class);
    }
}
