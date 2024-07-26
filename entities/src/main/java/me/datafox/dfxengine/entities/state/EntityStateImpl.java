package me.datafox.dfxengine.entities.state;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.EntityState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class EntityStateImpl implements EntityState {
    public String handle;
    public List<ComponentState> components;

    @Builder
    public EntityStateImpl(String handle, @Singular List<ComponentState> components) {
        this.handle = handle;
        this.components = new ArrayList<>(components);
    }
}
