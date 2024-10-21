package me.datafox.dfxengine.entities;

import lombok.Data;
import me.datafox.dfxengine.entities.api.Context;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.handles.HashHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;

/**
 * @author datafox
 */
@Data
public class EntityImpl implements Entity {
    private final Handle handle;

    private final HandleMap<EntityComponent> components;

    private final HandleMap<EntityComponent> componentsInternal;

    public EntityImpl(String handle, Context context) {
        this.handle = context.getHandles().getEntityHandle(handle);
        componentsInternal = new HashHandleMap<>(context.getHandles().getComponentSpace());
        components = componentsInternal.unmodifiable();
    }

    public void addComponent(EntityComponent component) {
        componentsInternal.putHandled(component);
    }
}
