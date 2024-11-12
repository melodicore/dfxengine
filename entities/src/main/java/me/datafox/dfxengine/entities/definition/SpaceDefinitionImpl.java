package me.datafox.dfxengine.entities.definition;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
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
public class SpaceDefinitionImpl implements SpaceDefinition {
    public String handle;

    public ArrayList<String> groups;

    public ArrayList<HandleDefinition> handles;

    @Builder
    public SpaceDefinitionImpl(String handle, @Singular List<String> groups, @Singular List<HandleDefinition> handles) {
        this.handle = handle;
        this.groups = new ArrayList<>(groups);
        this.handles = new ArrayList<>(handles);
    }

    @Component
    public static ClassTag getTag() {
        return new ClassTag("space", SpaceDefinitionImpl.class);
    }

    @Component
    public static DefaultElement getDefaultElement() {
        return new DefaultElement(SpaceDefinitionImpl.class, "handles", HandleDefinitionImpl.class);
    }
}
