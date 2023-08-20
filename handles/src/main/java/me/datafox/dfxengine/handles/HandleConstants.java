package me.datafox.dfxengine.handles;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Set;

/**
 * @author datafox
 */
public class HandleConstants {
    public static final String SPACES_ID;
    public static final String TAGS_ID;

    public static final Handle SPACES_HANDLE;
    public static final Handle TAGS_HANDLE;

    public static final Space SPACES;
    public static final Space TAGS;

    public static final Set<Space> SPACES_SET;

    static {
        SPACES_ID = "spaces";
        TAGS_ID = "tags";
        SPACES = SpaceImpl.bootstrap(SPACES_ID);
        SPACES_HANDLE = SPACES.getHandle(SPACES_ID);
        TAGS_HANDLE = SPACES.createHandle(TAGS_ID);
        TAGS = SpaceImpl
                .builder()
                .spaceHandle(TAGS_HANDLE)
                .build();
        SPACES_SET = Set.of(SPACES, TAGS);
    }
}
