package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

/**
 * @author datafox
 */
public interface EntityHandles {
    Space getEntitySpace();

    Space getComponentSpace();

    Space getDataSpace();

    Space getTreeSpace();

    default Handle getEntityHandle(String handle) {
        if(handle == null || handle.isBlank()) {
            return null;
        }
        return getEntitySpace().getOrCreateHandle(handle);
    }

    default Handle getComponentHandle(String handle) {
        if(handle == null || handle.isBlank()) {
            return null;
        }
        return getComponentSpace().getOrCreateHandle(handle);
    }

    default Handle getDataHandle(String handle) {
        if(handle == null || handle.isBlank()) {
            return null;
        }
        return getDataSpace().getOrCreateHandle(handle);
    }

    default Handle getTreeHandle(String handle) {
        if(handle == null || handle.isBlank()) {
            return null;
        }
        return getTreeSpace().getOrCreateHandle(handle);
    }
}
