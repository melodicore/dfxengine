package me.datafox.dfxengine.entities.state;

import lombok.*;
import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.entities.api.state.EntityState;

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
    public List<EntityState> singleEntities;
    public HashMap<String,List<EntityState>> multiEntities;

    @Builder
    public EngineStateImpl(@Singular List<EntityState> singleEntities, @Singular Map<String,List<EntityState>> multiEntities) {
        this.singleEntities = new ArrayList<>(singleEntities);
        this.multiEntities = new HashMap<>(multiEntities);
    }
}
