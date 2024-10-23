package me.datafox.dfxengine.entities.reference;

import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public class AllReference implements Reference {
    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return map.values().stream();
    }
}
