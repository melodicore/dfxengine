package me.datafox.dfxengine.entities.utils;

import me.datafox.dfxengine.entities.definition.NodeMappingImpl;
import me.datafox.dfxengine.entities.serialization.DefaultElement;

/**
 * @author datafox
 */
public class SerializationUtils {
    public static DefaultElement getNodeMappingsDefaultElement(Class<?> type) {
        return new DefaultElement(type, "mappings", NodeMappingImpl.class);
    }
}
