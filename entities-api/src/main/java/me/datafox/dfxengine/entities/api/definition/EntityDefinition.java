package me.datafox.dfxengine.entities.api.definition;

import java.util.List;

/**
 * @author datafox
 */
public interface EntityDefinition {
    String getHandle();

    List<ComponentDefinition> getComponents();
}
