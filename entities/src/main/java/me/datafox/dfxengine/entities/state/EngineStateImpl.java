package me.datafox.dfxengine.entities.state;

import lombok.*;
import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.entities.api.state.EntityState;

import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EngineStateImpl implements EngineState {
    @Singular
    private List<EntityState> singleEntities;
    @Singular
    private Map<String,List<EntityState>> multiEntities;
}
