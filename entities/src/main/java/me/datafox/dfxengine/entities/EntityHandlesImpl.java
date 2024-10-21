package me.datafox.dfxengine.entities;

import lombok.Data;
import lombok.Getter;
import me.datafox.dfxengine.entities.api.EntityHandles;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
@Data
public class EntityHandlesImpl implements EntityHandles {
    @Getter
    private static EntityHandlesImpl instance;

    private final Space entitySpace;

    private final Space componentSpace;

    private final Space dataSpace;

    private final Space treeSpace;

    @Inject
    public EntityHandlesImpl(HandleManager handleManager) {
        entitySpace = handleManager.getOrCreateSpace("entities");
        componentSpace = handleManager.getOrCreateSpace("components");
        dataSpace = handleManager.getOrCreateSpace("data");
        treeSpace = handleManager.getOrCreateSpace("trees");
        instance = this;
    }
}
