package me.datafox.dfxengine.entities.reference.selector;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.reference.Selector;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public class EverySelector implements Selector {
    @Override
    public boolean isSingle(boolean isEntity) {
        return false;
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        return map.values().stream();
    }
}
