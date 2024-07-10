package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityLink extends Handled {
    void link();

    void clear();
}
