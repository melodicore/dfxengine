package me.datafox.dfxengine.entities.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.EntityState;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityStateImpl implements EntityState {
    private String handle;
    private List<ComponentState> components;
}
