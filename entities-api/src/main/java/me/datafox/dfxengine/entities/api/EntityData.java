package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityData<T> extends Handled {
    Handle getTypeHandle();

    T getData();
}
