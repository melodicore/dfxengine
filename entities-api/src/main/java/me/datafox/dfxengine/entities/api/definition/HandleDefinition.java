package me.datafox.dfxengine.entities.api.definition;

import java.util.List;

/**
 * @author datafox
 */
public interface HandleDefinition {
    String getHandle();

    List<String> getGroups();

    List<String> getTags();
}
