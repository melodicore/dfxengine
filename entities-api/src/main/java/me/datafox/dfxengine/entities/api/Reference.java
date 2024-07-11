package me.datafox.dfxengine.entities.api;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Reference<T> {
    boolean isSingle();

    Stream<T> get(Engine engine);
}
