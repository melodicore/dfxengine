package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.Selector;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectorEntityReference implements Reference<Entity> {
    public Selector selector;

    @Override
    public boolean isSingle() {
        return selector.isSingle(true);
    }

    @Override
    public Stream<Entity> get(Engine engine) {
        return selector.select(engine.getEntities(), engine).flatMap(List::stream);
    }
}
