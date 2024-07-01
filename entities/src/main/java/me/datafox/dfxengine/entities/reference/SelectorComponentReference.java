package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.reference.ComponentReference;
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
public class SelectorComponentReference implements ComponentReference {
    private EntityReference entity;
    private Selector selector;

    @Override
    public boolean isSingle() {
        return getEntity().isSingle() && getSelector().isSingle();
    }

    @Override
    public Stream<EntityComponent> get(Engine engine) {
        return getEntity()
                .get(engine)
                .map(Entity::getComponents)
                .flatMap(map -> getSelector().select(map, engine));
    }
}
