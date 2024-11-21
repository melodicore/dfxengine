package me.datafox.dfxengine.entities.state;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.DataState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ComponentStateImpl implements ComponentState {
    public String handle;

    public ArrayList<DataState<?>> data;

    @Builder
    public ComponentStateImpl(String handle, @Singular List<DataState<?>> data) {
        this.handle = handle;
        this.data = new ArrayList<>(data);
    }
}
