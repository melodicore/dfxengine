package me.datafox.dfxengine.entities.api.reference;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface EntityReference {
    boolean isSingle();

    Stream<Entity> get(Engine engine);
}
