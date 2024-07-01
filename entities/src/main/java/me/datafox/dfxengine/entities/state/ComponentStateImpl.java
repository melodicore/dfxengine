package me.datafox.dfxengine.entities.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.DataState;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentStateImpl implements ComponentState {
    private String handle;
    private List<DataState> data;
}
