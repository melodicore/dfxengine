package me.datafox.dfxengine.entities.definition.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.SingleParameterOperationDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;
import me.datafox.dfxengine.entities.api.reference.DataReference;
import me.datafox.dfxengine.entities.system.ValueMapOperationSystem;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMapOperationSystemDefinition implements SystemDefinition {
    private int priority;
    private DataReference<ValueMap> inputs;
    private DataReference<ValueMap> outputs;
    private SingleParameterOperationDefinition operation;

    @Override
    public ValueMapOperationSystem build(Engine engine) {
        return new ValueMapOperationSystem(priority,
                inputs.get(engine).collect(Collectors.toList()),
                outputs.get(engine).collect(Collectors.toList()),
                operation.build(engine));
    }
}
