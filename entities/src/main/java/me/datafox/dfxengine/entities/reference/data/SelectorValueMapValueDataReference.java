package me.datafox.dfxengine.entities.reference.data;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.Selector;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SelectorValueMapValueDataReference implements Reference<Value> {
    private Reference<ValueMap> map;
    private Selector selector;

    @Override
    public boolean isSingle() {
        return map.isSingle() && selector.isSingle(false);
    }

    @Override
    public Stream<Value> get(Engine engine) {
        return map.get(engine).flatMap(map -> selector.select(map, engine));
    }
}
