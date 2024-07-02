package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityAction extends Handled {
    void schedule(Engine engine);

    void run(Engine engine);
}
