package me.datafox.dfxengine.entities.system;

import lombok.Getter;
import me.datafox.dfxengine.entities.api.EntitySystem;

/**
 * @author datafox
 */
@Getter
public abstract class AbstractEntitySystem implements EntitySystem {
    private final int priority;

    protected AbstractEntitySystem(int priority) {
        this.priority = priority;
    }
}
