package me.datafox.dfxengine.entities.system;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntitySystem;

/**
 * @author datafox
 */
@Getter
public abstract class AbstractEntitySystem implements EntitySystem {
    private final int priority;
    private final Engine engine;

    protected AbstractEntitySystem(int priority, Engine engine) {
        this.priority = priority;
        this.engine = engine;
    }
}
