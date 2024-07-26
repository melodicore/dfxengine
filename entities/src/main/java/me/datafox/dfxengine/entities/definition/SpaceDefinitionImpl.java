package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class SpaceDefinitionImpl implements SpaceDefinition {
    public String id;
    public List<String> groups;
    public List<HandleDefinition> handles;

    @Builder
    public SpaceDefinitionImpl(String id, @Singular List<String> groups, @Singular List<HandleDefinition> handles) {
        this.id = id;
        this.groups = new ArrayList<>(groups);
        this.handles = new ArrayList<>(handles);
    }
}
