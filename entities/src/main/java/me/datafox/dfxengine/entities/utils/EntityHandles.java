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
    private static Space actions;

    @Getter
    private static Handle valueType;
    @Getter
    private static Handle immutableValueType;
    @Getter
    private static Handle valueMapType;
    @Getter
    private static Handle immutableValueMapType;

    @Inject
    public EntityHandles(HandleManager handleManager) {
        EntityHandles.handleManager = handleManager;
        entities = handleManager.getOrCreateSpace("entities");
        components = handleManager.getOrCreateSpace("components");
        types = handleManager.getOrCreateSpace("types");
        data = handleManager.getOrCreateSpace("data");
        actions = handleManager.getOrCreateSpace("actions");

        valueType = types.getOrCreateHandle("value");
        immutableValueType = types.getOrCreateHandle("immutableValue");
        valueMapType = types.getOrCreateHandle("valueMap");
        immutableValueMapType = types.getOrCreateHandle("immutableValueMap");
    }
}
