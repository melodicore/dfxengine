package me.datafox.dfxengine.collections.test.handles;

import me.datafox.dfxengine.collections.HashHandleSet;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;

/**
 * @author datafox
 */
public class HashHandleSetTest extends AbstractHandleSetTest {
    @Override
    protected HandleSet getSet(Space space) {
        return new HashHandleSet(space);
    }
}
