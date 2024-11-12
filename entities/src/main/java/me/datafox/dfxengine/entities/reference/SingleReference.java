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
public class SingleReference implements Reference {
    public String handle;

    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return Stream.ofNullable(map.get(handle));
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("singleRef", SingleReference.class);
    }
}
