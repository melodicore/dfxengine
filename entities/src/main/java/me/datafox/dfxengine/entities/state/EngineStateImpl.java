package me.datafox.dfxengine.entities.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.entities.api.state.EntityState;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EngineStateImpl implements EngineState {
    private List<EntityState> entities;
}
