package me.datafox.dfxengine.entities.action;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityAction;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Getter
public abstract class AbstractAction implements EntityAction {
    private final Handle handle;

    protected AbstractAction(Handle handle) {
        this.handle = handle;
    }

    @Override
    public void schedule(Engine engine) {
        engine.scheduleAction(this);
    }
}
