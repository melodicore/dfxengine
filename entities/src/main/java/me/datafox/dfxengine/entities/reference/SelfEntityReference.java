package me.datafox.dfxengine.entities.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;
import me.datafox.dfxengine.entities.api.reference.EntityReference;

import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfEntityReference implements EntityReference {
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Stream<Entity> get(Engine engine) {
        return Stream.ofNullable(engine.getCurrentEntity());
    }
}
