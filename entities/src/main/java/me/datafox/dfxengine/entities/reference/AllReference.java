package me.datafox.dfxengine.entities.reference;

import me.datafox.dfxengine.entities.api.reference.Reference;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.stream.Stream;

/**
 * @author datafox
 */
public class AllReference implements Reference {
    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return map.values().stream();
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("allRef", AllReference.class);
    }
}
