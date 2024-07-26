package me.datafox.dfxengine.entities.reference.selector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Selector;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiHandleSelector implements Selector {
    public List<String> handles;

    @Override
    public boolean isSingle(boolean isEntity) {
        return false;
    }

    @Override
    public <T> Stream<T> select(HandleMap<T> map, Engine engine) {
        return handles
                .stream()
                .map(map::get)
                .filter(Objects::nonNull);
    }
}
