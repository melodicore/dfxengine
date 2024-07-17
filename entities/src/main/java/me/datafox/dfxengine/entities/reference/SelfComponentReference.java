package me.datafox.dfxengine.entities.reference;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.Reference;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@NoArgsConstructor
public class SelfComponentReference implements Reference<EntityComponent> {
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Stream<EntityComponent> get(Engine engine) {
        return Stream.ofNullable(engine.getCurrentComponent());
    }
}
