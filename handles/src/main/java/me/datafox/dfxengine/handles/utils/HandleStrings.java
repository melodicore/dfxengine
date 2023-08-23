package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

/**
 * @author datafox
 */
public class HandleStrings {
    private static final String SPACE_WITH_ID_ALREADY_PRESENT = "HandleManager already contains a space with id %s";
    private static final String HANDLE_WITH_ID_ALREADY_PRESENT = "Space %s already contains a handle with id %s";
    private static final String SPACE_MISMATCH_HANDLE_SET = "Tried to add handle %s to HandleSet with space %s";
    private static final String SPACE_MISMATCH_HANDLE_MAP = "Tried to add handle %s to HandleMap with space %s";

    public static String spaceWithIdAlreadyPresent(String spaceId) {
        return forString(SPACE_WITH_ID_ALREADY_PRESENT, spaceId);
    }

    public static String handleWithIdAlreadyPresent(Space space, String handleId) {
        return forSpaceAndString(HANDLE_WITH_ID_ALREADY_PRESENT, space, handleId);
    }

    public static String spaceMismatchHandleSet(Handle handle, Space space) {
        return forHandleAndSpace(SPACE_MISMATCH_HANDLE_SET, handle, space);
    }

    public static String spaceMismatchHandleMap(Handle handle, Space space) {
        return forHandleAndSpace(SPACE_MISMATCH_HANDLE_MAP, handle, space);
    }

    private static String forString(String str, String string) {
        return String.format(str, string);
    }

    private static String forSpaceAndString(String str, Space space, String string) {
        return String.format(str, space, string);
    }

    private static String forHandleAndSpace(String str, Handle handle, Space space) {
        return String.format(str, handle, space);
    }
}
