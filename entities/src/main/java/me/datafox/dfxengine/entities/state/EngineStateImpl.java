package me.datafox.dfxengine.entities.state;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.entities.api.state.EntityState;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class EngineStateImpl implements EngineState {
    public ArrayList<EntityState> entities;

    public HashMap<String, Integer> multiEntityCounts;

    @Builder
    public EngineStateImpl(@Singular List<EntityState> entities, @Singular Map<String, Integer> multiEntityCounts) {
        this.entities = new ArrayList<>(entities);
        this.multiEntityCounts = new HashMap<>(multiEntityCounts);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("engineState", EngineStateImpl.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return new DefaultElement(EngineStateImpl.class, "entities", EntityStateImpl.class);
    }
}
