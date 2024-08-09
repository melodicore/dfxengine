package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.EntityImpl;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.EntityDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class EntityDefinitionImpl implements EntityDefinition {
    public String handle;
    public List<ComponentDefinition> components;
    public boolean singleton;

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

    @Override
    public void append(EntityDefinition other) {
        Map<String,ComponentDefinition> map = components.stream()
                .collect(Collectors.toMap(
                        ComponentDefinition::getHandle,
                        Function.identity()));
        other.getComponents().forEach(component -> appendComponent(component, map));
    }

    private void appendComponent(ComponentDefinition component, Map<String,ComponentDefinition> map) {
        if(map.containsKey(component.getHandle())) {
            map.get(component.getHandle()).append(component);
            return;
        }
        components.add(component);
    }
}
