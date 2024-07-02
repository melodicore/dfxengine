package me.datafox.dfxengine.entities.definition.operation;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.OperationDefinition;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.operation.MappingOperationChain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MappingOperationChainDefinition implements OperationDefinition {
    @Singular
    private List<OperationDefinition> operations;

    @Override
    public int getParameterCount() {
        return operations.stream().mapToInt(OperationDefinition::getParameterCount).sum() + operations.size();
    }

    @Override
    public Operation build(Engine engine) {
        return new MappingOperationChain(operations
                .stream()
                .map(o -> o.build(engine))
                .collect(Collectors.toList()));
    }
}
