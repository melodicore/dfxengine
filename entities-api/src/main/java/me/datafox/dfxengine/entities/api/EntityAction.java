package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityAction extends Handled {
    void schedule(ActionParameters parameters);

    void run(ActionParameters parameters);

    void link();

    void clear();
}
