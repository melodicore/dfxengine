package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class EntityDefinitionImpl implements EntityDefinition {
    public String handle;

    public ArrayList<ComponentDefinition> components;

    @Builder
    public EntityDefinitionImpl(String handle, @Singular List<ComponentDefinition> components) {
        this.handle = handle;
        this.components = new ArrayList<>(components);
    }
}

