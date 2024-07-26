package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.EntityImpl;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
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
    private String handle;
    private List<ComponentDefinition> components;
    private boolean singleton;

    @Builder
    public EntityDefinitionImpl(String handle, @Singular List<ComponentDefinition> components, boolean singleton) {
        this.handle = handle;
        this.components = new ArrayList<>(components);
        this.singleton = singleton;
    }

    @Override
    public Entity build(Engine engine) {
        return new EntityImpl(this, engine);
    }
}
