package me.datafox.dfxengine.entities;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.EntityHandles;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.List;

/**
 * @author datafox
 */
@Component
@Getter
public class EngineImpl implements Engine {
    private final EntityHandles handles;

    private final HandleMap<List<Entity>> internalEntities;

    private final HandleMap<List<Entity>> internalEntitiesUL;

    private final HandleMap<List<Entity>> entities;

    @Inject
    public EngineImpl(EntityHandles handles) {
        this.handles = handles;
        internalEntities = new HashHandleMap<>(handles.getEntitySpace());
        internalEntitiesUL = new HashHandleMap<>(handles.getEntitySpace());
        entities = internalEntitiesUL.unmodifiable();
    }

    @Override
    public List<Entity> getEntities(String handle) {
        return entities.get(handle);
    }

    @Override
    public List<Entity> getEntities(Handle handle) {
        return entities.get(handle);
    }

    @Override
    public Entity getEntity(String handle) {
        List<Entity> list = entities.get(handle);
        if(list.size() != 1) {
            throw new IllegalArgumentException("not a single entity");
        }
        return list.get(0);
    }

    @Override
    public Entity getEntity(Handle handle) {
        List<Entity> list = entities.get(handle);
        if(list.size() != 1) {
            throw new IllegalArgumentException("not a single entity");
        }
        return list.get(0);
    }

    @Override
    public Entity createMultiEntity(String handle) {
        return createMultiEntity(handles.getEntityHandle(handle));
    }

    @Override
    public Entity createMultiEntity(Handle handle) {
        if(!handle.getTags().contains(handles.getMultiEntityTag())) {
            throw new IllegalArgumentException("not a multi entity");
        }
        return null;
    }
}
