package me.datafox.dfxengine.collections.test.handles;

import me.datafox.dfxengine.collections.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;

/**
 * @author datafox
 */
@Deprecated
public class TreeHandleMapTest extends AbstractHandleMapTest {
    @Override
    protected HandleMap<Object> getMap(Space space) {
        return new TreeHandleMap<>(space);
    }
}
