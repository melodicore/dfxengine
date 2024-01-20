package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.handles.api.Space;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
@SuppressWarnings({"MissingJavadoc", "SameParameterValue"})
public class HandleStrings {
    private static final String SPACE_WITH_ID_ALREADY_PRESENT = "HandleManager already contains a Space with id %s";
    private static final String HANDLE_WITH_ID_ALREADY_PRESENT = "Space %s already contains a Handle with id %s";
    private static final String REMOVE_HARDCODED_SPACE = "Tried to remove hardcoded Space %s";

    public static String spaceWithIdAlreadyPresent(String spaceId) {
        return forString(SPACE_WITH_ID_ALREADY_PRESENT, spaceId);
    }

    public static String handleWithIdAlreadyPresent(Space space, String handleId) {
        return forSpaceAndString(HANDLE_WITH_ID_ALREADY_PRESENT, space, handleId);
    }

    public static String removeHardcodedSpace(Space space) {
        return forSpace(REMOVE_HARDCODED_SPACE, space);
    }

    private static String forString(String str, String string) {
        return String.format(str, string);
    }

    private static String forSpaceAndString(String str, Space space, String string) {
        return String.format(str, space, string);
    }

    private static String forSpace(String str, Space space) {
        return String.format(str, space);
    }
}
