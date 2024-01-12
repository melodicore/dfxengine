package me.datafox.dfxengine.collections.test.handles;

import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.collection.TreeHandleSet;

/**
 * @author datafox
 */
public class TreeHandleSetTest extends AbstractHandleSetTest {
    @Override
    protected HandleSet getSet(Space space) {
        return new TreeHandleSet(space);
    }
}
