package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Selector {
    boolean isSingle(boolean isEntity);

    <T> Stream<T> select(HandleMap<T> map, Engine engine);
}
