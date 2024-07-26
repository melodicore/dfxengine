package me.datafox.dfxengine.entities.definition.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.action.ValueMapOperationAction;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.ActionDefinition;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapOperationActionDefinition implements ActionDefinition {
    public String handle;

    @Override
    public ValueMapOperationAction build(Engine engine) {
        return new ValueMapOperationAction(this, engine);
    }
}
