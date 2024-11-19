package me.datafox.dfxengine.entities.definition.node.group;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.component.Context;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeMapping;
import me.datafox.dfxengine.entities.api.node.NodeTree;
import me.datafox.dfxengine.entities.node.NodeGroupImpl;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.entities.utils.SerializationUtils;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class GroupOutputNodeDefinition implements NodeDefinition<NodeGroupImpl.OutputNode> {
    private transient Supplier<NodeGroupImpl.OutputNode> output;

    public ArrayList<NodeMapping> mappings;

    @Builder
    public GroupOutputNodeDefinition(@Singular List<NodeMapping> mappings) {
        this.mappings = new ArrayList<>(mappings);
    }

    @Override
    public NodeGroupImpl.OutputNode build(NodeTree tree, Context context) {
        return output.get();
    }

    public void setConstructor(Supplier<NodeGroupImpl.OutputNode> output) {
        this.output = output;
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("output", GroupOutputNodeDefinition.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return SerializationUtils.getNodeMappingsDefaultElement(GroupOutputNodeDefinition.class);
    }
}