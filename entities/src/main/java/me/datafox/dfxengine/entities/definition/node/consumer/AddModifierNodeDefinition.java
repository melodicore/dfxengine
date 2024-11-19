package me.datafox.dfxengine.entities.definition.node.consumer;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.consumer.AddModifierNode;
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
public class AddModifierNodeDefinition implements NodeDefinition<AddModifierNode> {
    public ArrayList<NodeMapping> mappings;

    public DataType<?> modified;

    public DataType<?> modifier;

    @Builder
    public AddModifierNodeDefinition(@Singular List<NodeMapping> mappings, DataType<?> modified, DataType<?> modifier) {
        this.mappings = new ArrayList<>(mappings);
        this.modified = modified;
        this.modifier = modifier;
    }

    @Override
    public AddModifierNode build(NodeTree tree, Context context) {
        return new AddModifierNode(tree, modified, modifier);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("addModifier", AddModifierNodeDefinition.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return SerializationUtils.getNodeMappingsDefaultElement(AddModifierNodeDefinition.class);
    }
}