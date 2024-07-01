package me.datafox.dfxengine.entities.reference.selector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class MultiTagSelector implements Selector {
    private List<String> tags;

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        return map.getByTags(getTags()).stream();
    }
}
