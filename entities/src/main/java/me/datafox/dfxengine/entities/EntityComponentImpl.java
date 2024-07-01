package me.datafox.dfxengine.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.EntityData;
import me.datafox.dfxengine.entities.api.StatefulEntityData;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.entities.state.ComponentStateImpl;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
public class EntityComponentImpl implements EntityComponent {
    private final Logger logger;
    @Getter
    private final Handle handle;
    private final HandleMap<HandleMap<EntityData<?>>> data;

    @Builder
    public EntityComponentImpl(Logger logger, Handle handle, @Singular List<EntityData<?>> data) {
        this.logger = logger;
        this.handle = handle;
        this.data = new TreeHandleMap<>(EntityHandles.getTypes());
        data.forEach(this::putData);
    }

    @Override
    public HandleMap<HandleMap<EntityData<?>>> getData() {
        return data.unmodifiable();
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
        if(!data.containsValue(entityData.getTypeHandle())) {
            data.put(entityData.getTypeHandle(), new HashHandleMap<>(EntityHandles.getData()));
        }
        data.get(entityData.getTypeHandle()).putHandled(entityData);
    }

    private void setDataState(DataState state) {
        if(!data.containsKey(state.getTypeHandle())) {
            logger.warn(String.format("State contains unknown data type %s", state.getHandle()));
            return;
        }
        HandleMap<EntityData<?>> map = data.get(state.getTypeHandle());
        if(!map.containsKey(state.getHandle())) {
            logger.warn(String.format("State contains unknown data value %s", state.getHandle()));
            return;
        }
        EntityData<?> d = map.get(state.getHandle());
        if(!(d instanceof StatefulEntityData)) {
            logger.warn(String.format("State contains non-stateful data value %s", state.getHandle()));
            return;
        }
        ((StatefulEntityData<?>) d).setState(state);
    }
}
