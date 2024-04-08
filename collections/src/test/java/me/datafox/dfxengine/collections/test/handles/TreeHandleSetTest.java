package me.datafox.dfxengine.collections.test.handles;

import me.datafox.dfxengine.collections.TreeHandleSet;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;

/**
 * @author datafox
 */
@Deprecated
public class TreeHandleSetTest extends AbstractHandleSetTest {
    @Override
    protected HandleSet getSet(Space space) {
        return new TreeHandleSet(space);
    }
}
