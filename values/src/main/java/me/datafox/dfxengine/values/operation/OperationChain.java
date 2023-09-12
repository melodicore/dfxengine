package me.datafox.dfxengine.values.operation;

import lombok.Getter;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author datafox
 */
public final class OperationChain implements Operation {
    private final List<Operation> operations;
    @Getter
    private final int parameterCount;

    private OperationChain(List<Operation> operations) {
        this.operations = operations;
        parameterCount = operations.stream().mapToInt(Operation::getParameterCount).sum();
    }

    @Override
    public Numeral apply(Numeral source, List<Numeral> parameters) throws IllegalArgumentException {
        int nextIndex = 0;
        for(Operation operation : operations) {
            source = operation.apply(source, parameters.subList(nextIndex, operation.getParameterCount() - 1));
            nextIndex = operation.getParameterCount();
        }
        return source;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Operation> operations;

        Builder() {
            operations = new ArrayList<>();
        }

        public Builder operation(SourceOperation operation) {
            this.operations.add(operation);
            return this;
        }

        public Builder operation(SingleParameterOperation operation) {
            this.operations.add(operation);
            return this;
        }

        public Builder operation(Operation operation) {
            this.operations.add(operation);
            return this;
        }

        public Builder operations(Collection<? extends Operation> operations) {
            if(operations == null) {
                return this;
            }
            this.operations.addAll(operations);
            return this;
        }

        public Builder clearOperations() {
            this.operations.clear();
            return this;
        }

        public OperationChain build() {
            return new OperationChain(operations);
        }
    }
}
