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

import static me.datafox.dfxengine.values.utils.internal.ValuesStrings.invalidParameterCount;

/**
 * An {@link Operation} that chains multiple operations together. The operations are executed in order, with the
 * previous one's output as the next one's input. Parameters are given in order, with the total parameter count being
 * the sum of all parameter counts of the specified operations. A simple {@link Builder} that works with method
 * references is included for convenience.
 *
 * @author datafox
 */
public final class OperationChain implements Operation {
    private final Logger logger;
    private final Operation[] operations;
    @Getter
    private final int parameterCount;

    /**
     * Public constructor for {@link OperationChain}.
     *
     * @param operations {@link Operation Operations} for this operation
     */
    public OperationChain(List<? extends Operation> operations) {
        logger = LoggerFactory.getLogger(OperationChain.class);
        this.operations = operations.toArray(Operation[]::new);
        parameterCount = operations.stream().mapToInt(Operation::getParameterCount).sum();
    }

    /**
     * Applies this operation to the specified source {@link Numeral} with the specified parameters.
     *
     * @param source source {@link Numeral} for this operation
     * @param parameters parameter {@link Numeral Numerals} for this operation
     * @return resulting {@link Numeral} of this operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to {@link #getParameterCount()}
     */
    @Override
    public Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters.length != parameterCount) {
            throw LogUtils.logExceptionAndGet(logger,
                    invalidParameterCount(parameterCount, parameters.length),
                    IllegalArgumentException::new);
        }
        int nextIndex = 0;
        for(Operation operation : operations) {
            source = operation.apply(source, Arrays.copyOfRange(
                    parameters, nextIndex, nextIndex + operation.getParameterCount()));
            nextIndex += operation.getParameterCount();
        }
        return source;
    }

    /**
     * Creates a builder for an {@link OperationChain}.
     *
     * @return builder for an {@link OperationChain}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link OperationChain}.
     */
    public static class Builder {
        private final List<Operation> operations;

        private Builder() {
            operations = new ArrayList<>();
        }

        /**
         * A {@link SourceOperation} for the operation chain.
         *
         * @param operation {@link SourceOperation} to add to the operation chain
         * @return this builder
         */
        public Builder operation(SourceOperation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * A {@link SingleParameterOperation} for the operation chain.
         *
         * @param operation {@link SingleParameterOperation} to add to the operation chain
         * @return this builder
         */
        public Builder operation(SingleParameterOperation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * A {@link DualParameterOperation} for the operation chain.
         *
         * @param operation {@link DualParameterOperation} to add to the operation chain
         * @return this builder
         */
        public Builder operation(DualParameterOperation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * An {@link Operation} for the operation chain.
         *
         * @param operation {@link Operation} to add to the operation chain
         * @return this builder
         */
        public Builder operation(Operation operation) {
            this.operations.add(operation);
            return this;
        }

        /**
         * {@link Operation Operations} for the operation chain.
         *
         * @param operations {@link Operation Operations} to add to the operation chain
         * @return this builder
         */
        public Builder operations(Collection<? extends Operation> operations) {
            if(operations == null) {
                return this;
            }
            this.operations.addAll(operations);
            return this;
        }

        /**
         * Clears all {@link Operation Operations} from the operation chain.
         *
         * @return this builder
         */
        public Builder clearOperations() {
            this.operations.clear();
            return this;
        }

        /**
         * Builds the operation chain.
         *
         * @return operation chain initialized by this builder
         */
        public OperationChain build() {
            return new OperationChain(operations);
        }
    }
}
