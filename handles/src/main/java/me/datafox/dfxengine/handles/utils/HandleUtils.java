package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import static me.datafox.dfxengine.handles.utils.HandleStrings.*;

/**
 * @author datafox
 */
public class HandleUtils {
    public static void checkId(String name, String id, Logger logger) {
        if(id == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    nullId(name),
                    NullPointerException::new);
        }
        if(id.isBlank()) {
            throw LogUtils.logExceptionAndGet(logger,
                    blankId(name),
                    IllegalArgumentException::new);
        }
        if(!id.matches("[ -9;-~]+")) {
            if(!id.contains(":") || id.substring(id.indexOf(':') + 1).contains(":")) {
                throw LogUtils.logExceptionAndGet(logger,
                        illegalId(name, id),
                        IllegalArgumentException::new);
            }
        }
    }

    public static void checkNullAndSpace(Handle handle, Space space, Logger logger) {
        if(handle == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    NULL_HANDLE,
                    NullPointerException::new);
        }
        if(!space.equals(handle.getSpace())) {
            throw LogUtils.logExceptionAndGet(logger,
                    spaceMismatch(handle, space),
                    IllegalArgumentException::new);
        }
    }

    public static void checkNullAndType(Object o, Logger logger) {
        if(o == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    NULL_KEY,
                    NullPointerException::new);
        }
        if(!(o instanceof Handle) && !(o instanceof String)) {
            throw LogUtils.logExceptionAndGet(logger,
                    keyType(o),
                    ClassCastException::new);
        }
    }

    public static void checkNullValue(Object value, Logger logger) {
        if(value == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    NULL_VALUE,
                    NullPointerException::new);
        }
    }
}
