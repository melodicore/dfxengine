package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.api.definition.HandleDefinition;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HandleDefinitionImpl implements HandleDefinition {
    private String id;
    @Singular
    private List<String> groups;
    @Singular
    private List<String> tags;
}
