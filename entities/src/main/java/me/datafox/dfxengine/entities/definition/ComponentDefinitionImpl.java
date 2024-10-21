package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ComponentDefinitionImpl implements ComponentDefinition {
    public String handle;

    public ArrayList<NodeTreeDefinition> trees;

    @Builder
    public ComponentDefinitionImpl(String handle, @Singular List<NodeTreeDefinition> trees) {
        this.handle = handle;
        this.trees = new ArrayList<>(trees);
    }
}
