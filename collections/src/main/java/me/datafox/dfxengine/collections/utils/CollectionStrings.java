package me.datafox.dfxengine.collections.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

/**
 * @author datafox
 */
public class CollectionStrings {
    private static final String SPACE_MISMATCH_HANDLE_SET = "Tried to add Handle %s to HandleSet with Space %s";
    private static final String NOT_HANDLED_TYPE = "Tried to add Object %s to HandleMap with putHandled() but it " +
            "does not implement me.datafox.dfxengine.handles.api.Handled";
    private static final String SPACE_MISMATCH_HANDLE_MAP = "Tried to add Handle %s to HandleMap with Space %s";

    public static <T> String notHandledType(T value) {
        return forObject(NOT_HANDLED_TYPE, value);
    }

    public static String spaceMismatchHandleSet(Handle handle, Space space) {
        return forHandleAndSpace(SPACE_MISMATCH_HANDLE_SET, handle, space);
    }

    public static String spaceMismatchHandleMap(Handle handle, Space space) {
        return forHandleAndSpace(SPACE_MISMATCH_HANDLE_MAP, handle, space);
    }

    private static String forObject(String str, Object object) {
        return String.format(str, object);
    }

    private static String forHandleAndSpace(String str, Handle handle, Space space) {
        return String.format(str, handle, space);
    }
}
