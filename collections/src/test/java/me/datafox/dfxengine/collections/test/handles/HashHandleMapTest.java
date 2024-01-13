package me.datafox.dfxengine.collections.test.handles;

import me.datafox.dfxengine.collections.HashHandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;

/**
 * @author datafox
 */
public class HashHandleMapTest extends AbstractHandleMapTest {
    @Override
    protected HandleMap<Object> getMap(Space space) {
        return new HashHandleMap<>(space);
    }
}
