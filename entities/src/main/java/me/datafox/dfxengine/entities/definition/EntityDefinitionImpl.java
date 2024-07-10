package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.EntityImpl;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityDefinitionImpl implements EntityDefinition {
    private String handle;
    @Singular
    private List<ComponentDefinition> components;
    private boolean singleton;

    @Override
    public Entity build(Engine engine) {
        return new EntityImpl(this, engine);
    }
}
