package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.injector.api.annotation.Component;

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

    @Component
    public static ClassTag getTag() {
        return new ClassTag("component", ComponentDefinitionImpl.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return new DefaultElement(ComponentDefinitionImpl.class, "trees", NodeTreeDefinitionImpl.class);
    }
}
