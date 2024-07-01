package me.datafox.dfxengine.entities.reference.selector;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.reference.Selector;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrSelector implements Selector {
    @Singular
    private List<Selector> selectors;

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        return getSelectors().stream().flatMap(s -> s.select(map, engine)).distinct();
    }
}
