package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.reference.Reference;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagReference implements Reference {
    public String tag;

    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return map.getByTag(tag).stream();
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("tagRef", TagReference.class);
    }
}
