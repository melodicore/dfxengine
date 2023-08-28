package me.datafox.dfxengine.handles.test;

import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.collection.HashHandleSet;

/**
 * @author datafox
 */
public class HashHandleSetTest extends AbstractHandleSetTest {
    @Override
    protected HandleSet getSet(Space space) {
        return new HashHandleSet(space);
    }
}
