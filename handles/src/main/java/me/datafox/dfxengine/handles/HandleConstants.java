package me.datafox.dfxengine.handles;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;

/**
 * Contains ids for the hardcoded {@link Space Spaces} in the {@link HandleManager}.
 *
 * @author datafox
 */
public class HandleConstants {
    /**
     * id for the {@link Space} that contains all {@link Handle Handles} used as Space identifiers.
     */
    public static final String SPACES_ID = "spaces";

    /**
     * id for the {@link Space} that contains all tag {@link Handle Handles}.
     */
    public static final String TAGS_ID = "tags";
}
