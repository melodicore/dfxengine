package me.datafox.dfxengine.entities.definition.operation;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.OperationDefinition;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.operation.OperationChain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationChainDefinition implements OperationDefinition {
    @Singular
    private List<OperationDefinition> operations;

    @Override
    public Operation build(Engine engine) {
        return new OperationChain(operations
                .stream()
                .map(o -> o.build(engine))
                .collect(Collectors.toList()));
    }
}
