package me.datafox.dfxengine.entities.reference.selector;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Selector;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class AndSelector implements Selector {
    private List<Selector> selectors;

    @Builder
    public AndSelector(@Singular List<Selector> selectors) {
        this.selectors = new ArrayList<>(selectors);
    }

    @Override
    public boolean isSingle(boolean isEntity) {
        return selectors.stream().allMatch(s -> s.isSingle(isEntity));
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        if(selectors.isEmpty()) {
            return Stream.empty();
        }
        if(selectors.size() == 1) {
            return selectors.get(0).select(map, engine);
        }
        return selectors.stream()
                .map(s -> s
                        .select(map, engine)
                        .collect(Collectors
                                .toCollection(LinkedHashSet::new)))
                .reduce(this::retainReducer)
                .stream()
                .flatMap(Set::stream);
    }

    private <T> LinkedHashSet<T> retainReducer(LinkedHashSet<T> ts, LinkedHashSet<T> ts2) {
        ts.retainAll(ts2);
        return ts;
    }
}
