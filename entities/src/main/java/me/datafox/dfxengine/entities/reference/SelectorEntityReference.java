package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.reference.EntityReference;
import me.datafox.dfxengine.entities.api.reference.Selector;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectorEntityReference implements EntityReference {
    private Selector selector;

    @Override
    public boolean isSingle() {
        return selector.isSingle();
    }

    @Override
    public Stream<Entity> get(Engine engine) {
        return selector.select(engine.getEntities(), engine);
    }
}
