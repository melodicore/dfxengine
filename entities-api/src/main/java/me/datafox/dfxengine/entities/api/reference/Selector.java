package me.datafox.dfxengine.entities.api.reference;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Selector {
    boolean isSingle(boolean isEntity);

    <T> Stream<T> select(HandleMap<T> map, Engine engine);
}
