package me.datafox.dfxengine.entities.reference;

import lombok.*;
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
public class ListReference implements Reference {
    public ArrayList<String> handles;

    @Builder
    public ListReference(@Singular List<String> handles) {
        this.handles = new ArrayList<>(handles);
    }

    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return handles.stream()
                .map(map::get)
                .flatMap(Stream::ofNullable);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("listRef", ListReference.class);
    }
}
