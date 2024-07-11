package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.Reference;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfEntityReference implements Reference<Entity> {
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Stream<Entity> get(Engine engine) {
        return Stream.ofNullable(engine.getCurrentEntity());
    }
}
