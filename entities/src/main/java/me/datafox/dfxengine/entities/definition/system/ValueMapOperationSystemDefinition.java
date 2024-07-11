package me.datafox.dfxengine.entities.definition.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Reference;
import me.datafox.dfxengine.entities.api.definition.MathContextDefinition;
import me.datafox.dfxengine.entities.api.definition.SingleParameterOperationDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;
import me.datafox.dfxengine.entities.system.ValueMapOperationSystem;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapOperationSystemDefinition implements SystemDefinition {
    private String handle;
    private int priority;
    private Reference<ValueMap> inputs;
    private Reference<ValueMap> outputs;
    private SingleParameterOperationDefinition operation;
    private MathContextDefinition context;

    @Override
    public ValueMapOperationSystem build(Engine engine) {
        return new ValueMapOperationSystem(this, engine);
    }
}
