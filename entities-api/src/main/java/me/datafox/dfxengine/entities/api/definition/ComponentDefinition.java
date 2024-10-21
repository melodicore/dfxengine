package me.datafox.dfxengine.entities.api.definition;

import java.util.List;

/**
 * @author datafox
 */
public interface ComponentDefinition {
    String getHandle();

    List<NodeTreeDefinition> getTrees();
}
