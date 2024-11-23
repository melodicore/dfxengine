package me.datafox.dfxengine.entities.state;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.entities.api.state.EntityState;
import me.datafox.dfxengine.entities.serialization.ClassTag;
import me.datafox.dfxengine.entities.serialization.DefaultElement;
import me.datafox.dfxengine.injector.api.annotation.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class EntityStateImpl implements EntityState {
    public String handle;

    public int index;

    public ArrayList<ComponentState> components;

    @Builder
    public EntityStateImpl(String handle, int index, @Singular List<ComponentState> components) {
        this.handle = handle;
        this.index = index;
        this.components = new ArrayList<>(components);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("entityState", EntityStateImpl.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return new DefaultElement(EntityStateImpl.class, "components", ComponentStateImpl.class);
    }
}
