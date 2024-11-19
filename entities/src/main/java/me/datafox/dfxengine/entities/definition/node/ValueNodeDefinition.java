package me.datafox.dfxengine.entities.definition.node;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.ValueNode;
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
public class ValueNodeDefinition implements NodeDefinition<ValueNode> {
    public ArrayList<NodeMapping> mappings;

    public boolean immutable;

    @Builder
    public ValueNodeDefinition(@Singular List<NodeMapping> mappings, boolean immutable) {
        this.mappings = new ArrayList<>(mappings);
        this.immutable = immutable;
    }

    @Override
    public ValueNode build(NodeTree tree, Context context) {
        return new ValueNode(tree, immutable);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("value", ValueNodeDefinition.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return SerializationUtils.getNodeMappingsDefaultElement(ValueNodeDefinition.class);
    }
}