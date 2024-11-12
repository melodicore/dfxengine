package me.datafox.dfxengine.entities.reference;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.reference.Reference;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.utils.StreamUtils;
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
public class AndReference implements Reference {
    public ArrayList<Reference> references;

    @Builder
    public AndReference(@Singular List<Reference> references) {
        this.references = new ArrayList<>(references);
    }

    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return references.stream()
                .map(r -> r.get(map))
                .reduce(StreamUtils::reduceSame)
                .orElse(Stream.empty());
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("andRef", AndReference.class);
    }
}
