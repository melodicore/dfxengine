package me.datafox.dfxengine.entities;

import lombok.Builder;
import lombok.Getter;
import me.datafox.dfxengine.entities.api.*;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.definition.ComponentDefinitionImpl;
import me.datafox.dfxengine.entities.state.ComponentStateImpl;
import me.datafox.dfxengine.entities.utils.EntityDataTypes;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class EntityComponentImpl implements EntityComponent {
    @Getter
    private final Handle handle;
    private final ComponentDefinitionImpl definition;
    private final Engine engine;
    private final Map<Class<?>, HandleMap<EntityData<?>>> data;
    private final HandleMap<EntityLink> links;
    private final HandleMap<EntityAction> actions;

    @Builder
    public EntityComponentImpl(ComponentDefinitionImpl definition, Engine engine) {
        handle = EntityHandles.getComponents().getOrCreateHandle(definition.getHandle());
        this.definition = definition;
        this.engine = engine;
        data = new HashMap<>();
        links = new HashHandleMap<>(EntityHandles.getLinks());
        actions = new HashHandleMap<>(EntityHandles.getActions());
        definition.getData().stream().map(d -> d.build(engine)).forEach(this::putData);
        definition.getLinks().stream().map(l -> l.build(engine)).forEach(links::putHandled);
        definition.getActions().stream().map(a -> a.build(engine)).forEach(actions::putHandled);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> HandleMap<EntityData<T>> getData(Class<T> type) {
        return ((HandleMap<EntityData<T>>) (HandleMap<?>) data.get(type)).unmodifiable();
    }

    @Override
    public HandleMap<EntityLink> getLinks() {
        return links.unmodifiable();
    }

    @Override
    public HandleMap<EntityAction> getActions() {
        return actions.unmodifiable();
    }

    @Override
    public void link() {
        links.values().forEach(EntityLink::link);
        actions.values().forEach(EntityAction::link);
    }

    @Override
    public void clear() {
        links.values().forEach(EntityLink::clear);
        actions.values().forEach(EntityAction::clear);
    }

    @Override
    public void setState(ComponentState state) {
        state.getData().forEach(this::setDataState);
    }

    @Override
    public ComponentState getState() {
        return ComponentStateImpl
                .builder()
                .handle(handle.getId())
                .data(data
                        .values()
                        .stream()
                        .map(HandleMap::values)
                        .flatMap(Collection::stream)
                        .filter(StatefulEntityData.class::isInstance)
                        .map(StatefulEntityData.class::cast)
                        .map(StatefulEntityData::getState)
                        .collect(Collectors.toList()))
                .build();
    }

    private void putData(EntityData<?> entityData) {
        if(!data.containsValue(entityData.getType())) {
            data.put(entityData.getType(), new HashHandleMap<>(EntityHandles.getData()));
        }
        data.get(entityData.getType()).putHandled(entityData);
    }

    private void setDataState(DataState state) {
        if(!data.containsKey(EntityDataTypes.getType(state.getTypeId()))) {
            engine.getLogger().warn(String.format("State contains unknown data type %s", state.getHandle()));
            return;
        }
        HandleMap<EntityData<?>> map = data.get(EntityDataTypes.getType(state.getTypeId()));
        if(!map.containsKey(state.getHandle())) {
            engine.getLogger().warn(String.format("State contains unknown data value %s", state.getHandle()));
            return;
        }
        EntityData<?> d = map.get(state.getHandle());
        if(!(d instanceof StatefulEntityData)) {
            engine.getLogger().warn(String.format("State contains non-stateful data value %s", state.getHandle()));
            return;
        }
        ((StatefulEntityData<?>) d).setState(state);
    }
}
