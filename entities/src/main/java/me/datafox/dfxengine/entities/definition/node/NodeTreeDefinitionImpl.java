package me.datafox.dfxengine.entities.definition.node;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.NodeDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;
import me.datafox.dfxengine.entities.api.node.NodeTreeAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class NodeTreeDefinitionImpl implements NodeTreeDefinition {
    public String handle;

    public int order;

    public ArrayList<NodeDefinition<?>> nodes;

    public ArrayList<NodeTreeAttribute> attributes;

    @Builder
    public NodeTreeDefinitionImpl(String handle,
                                  int order,
                                  @Singular List<NodeDefinition<?>> nodes,
                                  @Singular List<NodeTreeAttribute> attributes) {
        this.handle = handle;
        this.order = order;
        this.nodes = new ArrayList<>(nodes);
        this.attributes = new ArrayList<>(attributes);
    }
}
