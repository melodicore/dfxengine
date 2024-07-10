package me.datafox.dfxengine.entities.link;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityLink;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Getter
public abstract class AbstractLink implements EntityLink {
    private final Handle handle;
    private final Engine engine;

    protected AbstractLink(String handle, Engine engine) {
        this.handle = EntityHandles.getLinks().getOrCreateHandle(handle);
        this.engine = engine;
    }
}
