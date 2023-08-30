package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
public class HandleStrings {
    private static final String SPACE_WITH_ID_ALREADY_PRESENT = "HandleManager already contains a Space with id %s";
    private static final String HANDLE_WITH_ID_ALREADY_PRESENT = "Space %s already contains a Handle with id %s";
    private static final String SPACE_MISMATCH_HANDLE_SET = "Tried to add Handle %s to HandleSet with Space %s";
    private static final String SPACE_MISMATCH_HANDLE_MAP = "Tried to add Handle %s to HandleMap with Space %s";
    private static final String REMOVE_HARDCODED_SPACE = "Tried to remove hardcoded Space %s";
    private static final String NOT_HANDLED_TYPE = "Tried to add Object %s to HandleMap with putHandled() but it " +
            "does not implement me.datafox.dfxengine.handles.api.Handled";

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

    public static String removeHardcodedSpace(Space space) {
        return forSpace(REMOVE_HARDCODED_SPACE, space);
    }

    public static <T> String notHandledType(T value) {
        return forObject(NOT_HANDLED_TYPE, value);
    }

    private static String forString(String str, String string) {
        return String.format(str, string);
    }

    private static String forObject(String str, Object object) {
        return String.format(str, object);
    }

    private static String forSpaceAndString(String str, Space space, String string) {
        return String.format(str, space, string);
    }

    private static String forHandleAndSpace(String str, Handle handle, Space space) {
        return String.format(str, handle, space);
    }

    private static String forSpace(String str, Space space) {
        return String.format(str, space);
    }
}
