package me.datafox.dfxengine.entities.api.state;

import java.util.List;

/**
 * @author datafox
 */
public interface EntityState {
    String getHandle();

    int getIndex();

    List<ComponentState> getComponents();
}
