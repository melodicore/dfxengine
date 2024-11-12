package me.datafox.dfxengine.entities.reference;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.reference.Reference;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class MultiTagReference implements Reference {
    public ArrayList<String> tags;

    @Builder
    public MultiTagReference(@Singular List<String> tags) {
        this.tags = new ArrayList<>(tags);
    }

    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return map.getByTags(tags).stream();
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("multiTagRef", MultiTagReference.class);
    }
}
