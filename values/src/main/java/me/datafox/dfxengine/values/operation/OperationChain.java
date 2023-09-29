package me.datafox.dfxengine.values.operation;

import lombok.Getter;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.utils.LogUtils;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author datafox
 */
public final class OperationChain implements Operation {
    private final Logger logger;
    private final List<Operation> operations;
    @Getter
    private final int parameterCount;

    private OperationChain(List<Operation> operations) {
        logger = LoggerFactory.getLogger(OperationChain.class);
        this.operations = operations;
        parameterCount = operations.stream().mapToInt(Operation::getParameterCount).sum();
    }

    @Override
    public Numeral apply(Numeral source, Numeral ... parameters) throws IllegalArgumentException {
        if(parameters.length != parameterCount) {
            throw LogUtils.logExceptionAndGet(logger,
                    "invalid parameter count", IllegalArgumentException::new);
        }
        int nextIndex = 0;
        for(Operation operation : operations) {
            source = operation.apply(source, Arrays.copyOfRange(
                    parameters, nextIndex, nextIndex + operation.getParameterCount()));
            nextIndex += operation.getParameterCount();
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

        public Builder operation(DualParameterOperation operation) {
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
