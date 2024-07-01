package me.datafox.dfxengine.entities.api.reference;

import me.datafox.dfxengine.entities.api.Engine;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface DataReference<T> {
    boolean isSingle();

    Stream<T> get(Engine engine);
}
