package me.datafox.dfxengine.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.EntityState;
import me.datafox.dfxengine.entities.state.EntityStateImpl;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class EntityImpl implements Entity {
    private final Logger logger;
    @Getter
    private final Handle handle;
    private final HandleMap<EntityComponent> components;

    @Builder
    public EntityImpl(Logger logger, Handle handle, @Singular List<EntityComponent> components) {
        this.logger = logger;
        this.handle = handle;
        this.components = new TreeHandleMap<>(EntityHandles.getComponents());
        components.forEach(this.components::putHandled);
    }

    @Override
    public HandleMap<EntityComponent> getComponents() {
        return components.unmodifiable();
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
            logger.warn(String.format("State contains unknown component %s", state.getHandle()));
            return;
        }
        components.get(state.getHandle()).setState(state);
    }
}
