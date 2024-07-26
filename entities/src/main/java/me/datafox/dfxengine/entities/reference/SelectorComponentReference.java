package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.Selector;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectorComponentReference implements Reference<EntityComponent> {
    public Reference<Entity> entity;
    public Selector selector;

    @Override
    public boolean isSingle() {
        return getEntity().isSingle() && getSelector().isSingle(false);
    }

    @Override
    public Stream<EntityComponent> get(Engine engine) {
        return getEntity()
                .get(engine)
                .map(Entity::getComponents)
                .flatMap(map -> getSelector().select(map, engine));
    }
}
