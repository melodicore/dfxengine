package me.datafox.dfxengine.entities;

import lombok.Builder;
import lombok.Getter;
import me.datafox.dfxengine.entities.api.*;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.EntityState;
import me.datafox.dfxengine.entities.definition.EntityDefinitionImpl;
import me.datafox.dfxengine.entities.state.EntityStateImpl;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class EntityImpl implements Entity {
    @Getter
    private final Handle handle;
    private final EntityDefinitionImpl definition;
    private final Engine engine;
    private final HandleMap<EntityComponent> components;

    @Builder
    public EntityImpl(EntityDefinitionImpl definition, Engine engine) {
        handle = EntityHandles.getEntities().getOrCreateHandle(definition.getHandle());
        if(definition.isSingleton()) {
            handle.getTags().add(EntityHandles.getSingleTag());
        }
        this.definition = definition;
        this.engine = engine;
        components = new TreeHandleMap<>(EntityHandles.getComponents());
        definition.getComponents().stream().map(c -> c.build(engine)).forEach(this.components::putHandled);
    }

    @Override
    public HandleMap<EntityComponent> getComponents() {
        return components.unmodifiable();
    }

    @Override
    public boolean isSingleton() {
        return definition.isSingleton();
    }

    @Override
    public int getIndex() {
        return isSingleton() ? 0 : engine.getEntities().get(handle).indexOf(this);
    }

    @Override
    public void setState(EntityState state) {
        state.getComponents().forEach(this::setComponentState);
    }

    @Override
    public EntityState getState() {
        return EntityStateImpl
                .builder()
                .handle(handle.getId())
                .components(components
                        .values()
                        .stream()
                        .map(EntityComponent::getState)
                        .collect(Collectors.toList()))
                .build();
    }

    private void setComponentState(ComponentState state) {
        if(!components.containsKey(state.getHandle())) {
            engine.getLogger().warn(String.format("State contains unknown component %s", state.getHandle()));
            return;
        }
        components.get(state.getHandle()).setState(state);
    }
}
