package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@FunctionalInterface
public interface Reference {
    <T> Stream<T> get(HandleMap<T> map);
}
