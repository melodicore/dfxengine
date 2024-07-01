package me.datafox.dfxengine.entities.api.reference;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface ComponentReference {
    boolean isSingle();

    Stream<EntityComponent> get(Engine engine);
}
