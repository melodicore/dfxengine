package me.datafox.dfxengine.entities.component;

import lombok.Data;
import me.datafox.dfxengine.entities.api.component.EntityHandles;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
@Data
public class EntityHandlesImpl implements EntityHandles {
    private final HandleManager handleManager;

    private final Space entitySpace;

    private final Space componentSpace;

    private final Space dataSpace;

    private final Space treeSpace;

    private final Handle multiEntityTag;

    @Inject
    public EntityHandlesImpl(HandleManager handleManager) {
        this.handleManager = handleManager;
        entitySpace = handleManager.getOrCreateSpace("entities");
        componentSpace = handleManager.getOrCreateSpace("components");
        dataSpace = handleManager.getOrCreateSpace("data");
        treeSpace = handleManager.getOrCreateSpace("trees");
        multiEntityTag = handleManager.getTagSpace().getOrCreateHandle("multi");
    }

    public void createSpaces(SpaceDefinition definition) {
        Space space = handleManager.getOrCreateSpace(definition.getHandle());
        definition.getGroups().forEach(space::getOrCreateGroup);
        if(handleManager.getTagSpace().equals(space)) {
            definition.getHandles()
                    .stream()
                    .map(HandleDefinition::getHandle)
                    .forEach(space::getOrCreateHandle);
        }
        definition.getHandles().forEach(handle ->
                createHandle(space, handle, handleManager.getSpaceSpace().equals(space)));
    }

    private void createHandle(Space space, HandleDefinition definition, boolean spaceSpace) {
        Handle handle;
        if(spaceSpace) {
            handle = handleManager.getOrCreateSpace(definition.getHandle()).getHandle();
        } else {
            handle = space.getOrCreateHandle(definition.getHandle());
        }
        definition.getGroups().stream()
                .map(space::getOrCreateGroup)
                .forEach(group -> group.getHandles().add(handle));
        definition.getTags().forEach(handle.getTags()::add);
    }
}
