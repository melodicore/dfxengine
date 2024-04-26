package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
@SuppressWarnings({"MissingJavadoc", "SameParameterValue"})
public class HandleStrings {
    public static final String HANDLE = "Handle";
    public static final String SUBHANDLE = "Subhandle";
    public static final String SPACE = "Space";
    public static final String GROUP = "Group";
    public static final String SPACE_SPACE_ID = "spaces";
    public static final String TAG_SPACE_ID = "tags";
    public static final String INITIALIZING = "Initializing handle manager";
    public static final String REFLECTION = "Could not initialize handle manager, reflection failed";
    public static final String SUBHANDLE_SUBHANDLE = "A subhandle may not have subhandles of its own";
    public static final String CREATE_SPACE_HANDLE = "Handles cannot be created in " +
            "the space space manually, use HandleManager.createSpace(String) instead";
    public static final String GET_CREATE_SPACE_HANDLE = "Handles cannot be created in " +
            "the space space manually, use HandleManager.getOrCreateSpace(String) instead";
    public static final String CREATE_SPACE_SUBHANDLE = "Subhandles cannot be " +
            "created on space handles manually, use Space.createGroup(String) instead";
    public static final String GET_CREATE_SPACE_SUBHANDLE = "Subhandles cannot be " +
            "created on space handles manually, use Space.getOrCreateGroup(String) instead";
    public static final String SUBHANDLE_ID = "Subhandle id may not contain a colon";
    public static final String GROUP_ID = "Group id may not contain a colon";
    public static final String NULL_HANDLE = "Handle must not be null";
    public static final String NULL_KEY = "Key must not be null";
    public static final String NULL_VALUE = "Value must not be null";
    public static final String UNMODIFIABLE_SET = "This HandleSet is unmodifiable";
    public static final String UNMODIFIABLE_MAP = "This HandleMap is unmodifiable";

    private static final String NULL_ID = "%s id may not be null";
    private static final String BLANK_ID = "%s id may not be empty or blank";
    private static final String ILLEGAL_ID = "%s id \"%s\" contains illegal characters. " +
            "An id may only contain printable ASCII characters and no more than one colon (:)";
    private static final String UNAVAILABLE_ID = "%s id \"%s\" is already in use";
    private static final String SPACE_MISMATCH = "Handle \"%s\" is from space \"%s\" " +
            "but was attempted to add into a collection associated with space \"%s\"";
    private static final String KEY_TYPE = "Key %s of class " +
            "%s does not extend Handle and is not a String";
    private static final String NOT_TAG = "Handle %s is not a tag";
    private static final String ORDERED_SPACES = "Spaces are %s";
    private static final String ORDERED_HANDLES_SPACES = "Handles in spaces are %s";
    private static final String ORDERED_HANDLES_GROUPS = "Handles in groups are %s";
    private static final String ORDERED_GROUPS = "Groups are %s";
    private static final String ORDERED_SUBHANDLES = "Subhandles are %s";
    private static final String ORDERED_TAGS = "Tags are %s";
    private static final String ORDERED = "ordered";
    private static final String UNORDERED = "unordered";
    private static final String NOT_HANDLED = "Object %s of class %s " +
            "does not extend Handled, use HandleMap.put(Handle,T) instead";

    public static String nullId(String name) {
        return String.format(NULL_ID, name);
    }

    public static String blankId(String name) {
        return String.format(BLANK_ID, name);
    }

    public static String illegalId(String name, String id) {
        return String.format(ILLEGAL_ID, name, id);
    }

    public static String spaceMismatch(Handle handle, Space space) {
        return String.format(SPACE_MISMATCH, handle.getId(), handle.getSpace().getHandle().getId(), space.getHandle().getId());
    }

    public static String keyType(Object o) {
        return String.format(KEY_TYPE, o, o.getClass().getSimpleName());
    }

    public static String notTag(Handle handle) {
        return String.format(NOT_TAG, handle);
    }

    public static String unavailableId(String name, String id) {
        return String.format(UNAVAILABLE_ID, name, id);
    }

    public static String spaceConfiguration(boolean ordered) {
        return String.format(ORDERED_SPACES, ordered ? ORDERED : UNORDERED);
    }

    public static String handleInSpaceConfiguration(boolean ordered) {
        return String.format(ORDERED_HANDLES_SPACES, ordered ? ORDERED : UNORDERED);
    }

    public static String handleInGroupConfiguration(boolean ordered) {
        return String.format(ORDERED_HANDLES_GROUPS, ordered ? ORDERED : UNORDERED);
    }

    public static String groupConfiguration(boolean ordered) {
        return String.format(ORDERED_GROUPS, ordered ? ORDERED : UNORDERED);
    }

    public static String subhandleConfiguration(boolean ordered) {
        return String.format(ORDERED_SUBHANDLES, ordered ? ORDERED : UNORDERED);
    }

    public static String tagConfiguration(boolean ordered) {
        return String.format(ORDERED_TAGS, ordered ? ORDERED : UNORDERED);
    }

    public static String notHandled(Object o) {
        return String.format(NOT_HANDLED, o, o.getClass().getSimpleName());
    }
}
