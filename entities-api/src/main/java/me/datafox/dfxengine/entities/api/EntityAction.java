package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityAction extends Handled {
    void schedule();

    void run();

    void link();

    void clear();
}
