package me.datafox.dfxengine.entities.api.definition;

import java.util.List;

/**
 * @author datafox
 */
public interface SpaceDefinition {
    String getHandle();

    List<String> getGroups();

    List<HandleDefinition> getHandles();
}
