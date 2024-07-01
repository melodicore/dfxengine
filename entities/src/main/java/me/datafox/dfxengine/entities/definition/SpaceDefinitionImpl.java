package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpaceDefinitionImpl implements SpaceDefinition {
    private String id;
    @Singular
    private List<String> groups;
    @Singular
    private List<HandleDefinition> handles;
}
