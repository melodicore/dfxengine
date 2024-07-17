package me.datafox.dfxengine.entities.utils;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
import me.datafox.dfxengine.handles.api.Group;
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
    private static Space data;
    @Getter
    private static Space links;
    @Getter
    private static Space actions;

    @Getter
    private static Handle singleTag;
    @Getter
    private static Handle immutableTag;

    @Inject
    public EntityHandles(HandleManager handleManager) {
        EntityHandles.handleManager = handleManager;

        entities = handleManager.getOrCreateSpace("entities");
        components = handleManager.getOrCreateSpace("components");
        data = handleManager.getOrCreateSpace("data");
        links = handleManager.getOrCreateSpace("links");
        actions = handleManager.getOrCreateSpace("actions");

        singleTag = handleManager.getTagSpace().getOrCreateHandle("singleEntity");
        immutableTag = handleManager.getTagSpace().getOrCreateHandle("immutableData");
    }

    public static void setSpace(SpaceDefinition definition) {
        Space space = handleManager.getOrCreateSpace(definition.getId());
        definition.getGroups().forEach(space::getOrCreateGroup);
        definition.getHandles().forEach(handle -> EntityHandles.setHandle(space, handle));
    }

    public static void clear() {
        handleManager.getSpaces().values().forEach(EntityHandles::clearSpace);
    }

    private static void setHandle(Space space, HandleDefinition definition) {
        Handle handle = space.getOrCreateHandle(definition.getId());
        definition.getGroups().stream().map(space::getOrCreateGroup).forEach(group -> group.getHandles().add(handle));
        definition.getTags().forEach(handle.getTags()::add);
    }

    private static void clearSpace(Space space) {
        space.getGroups().values().forEach(EntityHandles::clearGroup);
        space.getAllHandles().forEach(EntityHandles::clearHandle);
    }

    private static void clearGroup(Group group) {
        group.getHandles().clear();
    }

    private static void clearHandle(Handle handle) {
        handle.getTags().clear();
    }
}
