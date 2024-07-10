package me.datafox.dfxengine.entities.reference.selector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.reference.Selector;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagSelector implements Selector {
    private String tag;

    @Override
    public boolean isSingle(boolean isEntity) {
        return false;
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        return map.getByTag(getTag()).stream();
    }
}
