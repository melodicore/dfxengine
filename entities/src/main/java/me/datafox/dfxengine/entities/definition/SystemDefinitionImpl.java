package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.NodeTreeDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.math.api.NumeralType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class SystemDefinitionImpl implements SystemDefinition {
    public NumeralType intervalType;

    public String intervalValue;

    public ArrayList<NodeTreeDefinition> trees;

    @Builder
    public SystemDefinitionImpl(NumeralType intervalType, String intervalValue, @Singular List<NodeTreeDefinition> trees) {
        this.intervalType = intervalType;
        this.intervalValue = intervalValue;
        this.trees = new ArrayList<>(trees);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("system", SystemDefinitionImpl.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return new DefaultElement(SystemDefinitionImpl.class, "trees", NodeTreeDefinitionImpl.class);
    }
}