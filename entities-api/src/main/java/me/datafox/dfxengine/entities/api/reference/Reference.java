package me.datafox.dfxengine.entities.api.reference;

import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@FunctionalInterface
public interface Reference {
    <T> Stream<T> get(HandleMap<T> map);
}
