package me.datafox.dfxengine.entities.definition.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.action.RemoveEntityAction;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.ActionDefinition;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveEntityActionDefinition implements ActionDefinition {
    private String handle;

    @Override
    public RemoveEntityAction build(Engine engine) {
        return new RemoveEntityAction(this, engine);
    }
}
