package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityData<T> extends Handled {
    Class<T> getType();

    T getData();
}
