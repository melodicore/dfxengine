package me.datafox.dfxengine.entities.reference.selector;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Selector;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class OrSelector implements Selector {
    public List<Selector> selectors;

    @Builder
    public OrSelector(@Singular List<Selector> selectors) {
        this.selectors = new ArrayList<>(selectors);
    }

    @Override
    public boolean isSingle(boolean isEntity) {
        if(selectors.size() == 1) {
            return selectors.get(0).isSingle(isEntity);
        }
        return false;
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        return getSelectors().stream().flatMap(s -> s.select(map, engine)).distinct();
    }
}
