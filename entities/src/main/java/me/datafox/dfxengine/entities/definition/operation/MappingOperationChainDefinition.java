package me.datafox.dfxengine.entities.definition.operation;

import lombok.*;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.OperationDefinition;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.operation.MappingOperationChain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class MappingOperationChainDefinition implements OperationDefinition {
    public List<OperationDefinition> operations;

    @Builder
    public MappingOperationChainDefinition(@Singular List<OperationDefinition> operations) {
        this.operations = new ArrayList<>(operations);
    }

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
