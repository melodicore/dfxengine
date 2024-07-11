package me.datafox.dfxengine.entities.definition.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.action.ValueOperationAction;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.ActionDefinition;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueOperationActionDefinition implements ActionDefinition {
    private String handle;

    @Override
    public ValueOperationAction build(Engine engine) {
        return new ValueOperationAction(this, engine);
    }
}
