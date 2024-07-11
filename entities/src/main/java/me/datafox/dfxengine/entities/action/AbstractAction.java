package me.datafox.dfxengine.entities.action;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.ActionParameters;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityAction;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Getter
public abstract class AbstractAction implements EntityAction {
    private final Handle handle;
    private final Engine engine;

    protected AbstractAction(String handle, Engine engine) {
        this.handle = EntityHandles.getActions().getOrCreateHandle(handle);
        this.engine = engine;
    }

    @Override
    public void schedule(ActionParameters parameters) {
        engine.scheduleAction(this, parameters);
    }
}
