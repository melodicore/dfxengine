package me.datafox.dfxengine.entities.utils;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class EntityHandles {
    @Getter
    private static HandleManager handleManager;

    @Getter
    private static Space entities;
    @Getter
    private static Space components;
    @Getter
    private static Space types;
    @Getter
    private static Space data;

    @Getter
    private static Handle valueType;
    @Getter
    private static Handle valueMapType;
    @Getter
    private static Handle modifierType;

    @Inject
    public EntityHandles(HandleManager handleManager) {
        EntityHandles.handleManager = handleManager;
        entities = handleManager.getOrCreateSpace("entities");
        components = handleManager.getOrCreateSpace("components");
        types = handleManager.getOrCreateSpace("types");
        data = handleManager.getOrCreateSpace("data");

        valueType = types.getOrCreateHandle("value");
        valueMapType = types.getOrCreateHandle("valueMap");
    }
}
