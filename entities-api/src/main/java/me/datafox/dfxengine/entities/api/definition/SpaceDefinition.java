package me.datafox.dfxengine.entities.api.definition;

import java.util.List;

/**
 * @author datafox
 */
public interface SpaceDefinition {
    String getId();

    List<String> getGroups();

    List<HandleDefinition> getHandles();
}
