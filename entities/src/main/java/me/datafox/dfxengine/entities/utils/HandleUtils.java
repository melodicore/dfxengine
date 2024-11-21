package me.datafox.dfxengine.entities.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;

/**
 * @author datafox
 */
public class HandleUtils {
    public static String toIdWithSpace(Handle handle) {
        return handle.getSpace().getHandle().getId() + ":" + handle.getId();
    }

    public static Handle fromIdWithSpace(String idWithSpace, HandleManager handleManager) {
        String[] split = idWithSpace.split(":", 2);
        Space space = handleManager.getOrCreateSpace(split[0]);
        return space.getOrCreateHandle(split[1]);
    }
}
