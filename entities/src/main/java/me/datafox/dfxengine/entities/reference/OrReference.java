package me.datafox.dfxengine.entities.reference;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.utils.StreamUtils;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class OrReference implements Reference {
    public ArrayList<Reference> references;

    @Builder
    public OrReference(@Singular List<Reference> references) {
        this.references = new ArrayList<>(references);
    }

    @Override
    public <T> Stream<T> get(HandleMap<T> map) {
        return references.stream()
                .flatMap(r -> r.get(map))
                .flatMap(StreamUtils.distinctSame());
    }
}