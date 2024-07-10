package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.reference.ComponentReference;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfComponentReference implements ComponentReference {
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Stream<EntityComponent> get(Engine engine) {
        return Stream.ofNullable(engine.getCurrentComponent());
    }
}
