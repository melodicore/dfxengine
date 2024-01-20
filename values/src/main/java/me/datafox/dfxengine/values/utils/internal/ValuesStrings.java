package me.datafox.dfxengine.values.utils.internal;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
@SuppressWarnings("SameParameterValue")
public class ValuesStrings {
    public static final String OVERFLOW = "Numeral overflow or underflow";
    public static final String MIDWAY_EXCEPTION = "An exception may cause this operation to fail midway";
    public static final String FUTURE_REFERENCE = "Referenced a future operation";

    private static final String HANDLE_IGNORED =
            "The specified Handle %s is ignored and the Value %s's Handle is used as a key instead, " +
                    "consider using ValueMap.putHandled(Value) instead of ValueMap.put(Handle, Value)";
    private static final String SPACE_IGNORED = "Handles from Spaces other than %s are ignored";
    private static final String INVALID_PARAMETER_COUNT = "Expected %s parameters but %s were present instead";

    public static String handleIgnored(Handle handle, Value value) {
        return forHandleAndValue(HANDLE_IGNORED, handle, value);
    }

    public static String spaceIgnored(Space space) {
        return forSpace(SPACE_IGNORED, space);
    }

    public static String invalidParameterCount(int expected, int actual) {
        return forTwoInts(SPACE_IGNORED, expected, actual);
    }

    private static String forHandleAndValue(String str, Handle handle, Value value) {
        return String.format(str, handle, value);
    }

    private static String forSpace(String str, Space space) {
        return String.format(str, space);
    }

    private static String forTwoInts(String str, int int1, int int2) {
        return String.format(str, int1, int2);
    }
}
