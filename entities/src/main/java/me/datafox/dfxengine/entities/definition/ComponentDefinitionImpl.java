package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.api.definition.ActionDefinition;
import me.datafox.dfxengine.entities.api.definition.ComponentDefinition;
import me.datafox.dfxengine.entities.api.definition.DataDefinition;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentDefinitionImpl implements ComponentDefinition {
    private String handle;
    @Singular("data")
    private List<DataDefinition> data;
    @Singular
    private List<ActionDefinition> actions;
}
