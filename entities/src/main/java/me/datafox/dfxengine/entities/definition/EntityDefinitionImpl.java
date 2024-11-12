package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
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
public class EntityDefinitionImpl implements EntityDefinition {
    public String handle;

    public ArrayList<ComponentDefinition> components;

    @Builder
    public EntityDefinitionImpl(String handle, @Singular List<ComponentDefinition> components) {
        this.handle = handle;
        this.components = new ArrayList<>(components);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("entity", EntityDefinitionImpl.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return new DefaultElement(EntityDefinitionImpl.class, "components", ComponentDefinitionImpl.class);
    }
}

