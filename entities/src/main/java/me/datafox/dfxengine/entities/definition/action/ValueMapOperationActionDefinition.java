package me.datafox.dfxengine.entities.definition.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.action.ValueMapOperationAction;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.ActionDefinition;
import me.datafox.dfxengine.entities.api.definition.MathContextDefinition;
import me.datafox.dfxengine.entities.api.definition.OperationDefinition;
import me.datafox.dfxengine.entities.api.reference.DataReference;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.List;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapOperationActionDefinition implements ActionDefinition {
    private String handle;
    private OperationDefinition operation;
    private List<DataReference<Value>> inputs;
    private DataReference<ValueMap> outputs;
    private MathContextDefinition context;

    @Override
    public ValueMapOperationAction build(Engine engine) {
        return new ValueMapOperationAction(this, engine);
    }
}
