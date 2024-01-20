package me.datafox.dfxengine.values.modifier;

import me.datafox.dfxengine.utils.LogUtils;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import me.datafox.dfxengine.values.operation.MappingOperationChain;
import me.datafox.dfxengine.values.operation.MappingOperationChain.SpecialNumeral;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.datafox.dfxengine.values.utils.internal.ValuesStrings.FUTURE_REFERENCE;

/**
 * An extension of {@link OperationModifier} that works with {@link MappingOperationChain}. Includes
 * {@link SpecialValue}, the {@link Value} equivalent of {@link SpecialNumeral}, with the static convenience methods
 * {@link #sourceValue()} and {@link #resultValue(int)}. Has a {@link Builder} for convenience that tracks the number
 * of parameters when using {@link Builder#operation(Operation, Value...)}, throwing {@link IllegalArgumentException} if
 * the amount of parameters is not correct. The builder also throws IllegalArgumentException if a SpecialValue is given
 * as a parameter that refers to a future operation.
 *
 * @author datafox
 */
public class MappingOperationModifier extends OperationModifier {
    /**
     * @return {@link SpecialValue} that refers to the source {@link Value}
     */
    public static SpecialValue sourceValue() {
        return new SpecialValue(MappingOperationChain.sourceNumeral());
    }

    /**
     * @param operationIndex index of the {@link Operation} to refer to
     * @return {@link SpecialValue} that refers to the result of the specified {@link Operation}
     */
    public static SpecialValue resultValue(int operationIndex) {
        return new SpecialValue(MappingOperationChain.resultNumeral(operationIndex));
    }

    /**
     * @param priority priority for this modifier
     * @param operation {@link MappingOperationChain} for this modifier
     * @param parameters parameter {@link Value Values} for this modifier
     */
    public MappingOperationModifier(int priority, MappingOperationChain operation, Value ... parameters) {
        super(LoggerFactory.getLogger(MappingOperationModifier.class), priority, operation, parameters);
    }

    /**
     * @param priority priority for this {@link Builder}
     * @return {@link Builder} instance
     */
    public static Builder builder(int priority) {
        return new Builder(priority);
    }

    /**
     * A Builder for {@link MappingOperationModifier}.
     */
    public static class Builder {
        private final int priority;
        private final MappingOperationChain.Builder operationBuilder;
        private final List<Value> parameters;

        private Builder(int priority) {
            this.priority = priority;
            operationBuilder = MappingOperationChain.builder();
            parameters = new ArrayList<>();
        }

        /**
         * @param operation {@link SourceOperation} to add to the {@link MappingOperationModifier}
         * @param parameter parameter {@link Value} for the {@link SourceOperation}
         * @return this builder
         */
        public Builder operation(SourceOperation operation, Value parameter) {
            checkSpecialValue(parameter);
            operationBuilder.operation(operation);
            parameters.add(parameter);
            return this;
        }

        /**
         * @param operation {@link SingleParameterOperation} to add to the {@link MappingOperationModifier}
         * @param parameter1 first parameter {@link Value} for the {@link SingleParameterOperation}
         * @param parameter2 second parameter {@link Value} for the {@link SingleParameterOperation}
         * @return this builder
         */
        public Builder operation(SingleParameterOperation operation, Value parameter1, Value parameter2) {
            checkSpecialValue(parameter1);
            checkSpecialValue(parameter2);
            operationBuilder.operation(operation);
            parameters.addAll(List.of(parameter1, parameter2));
            return this;
        }

        /**
         * @param operation {@link DualParameterOperation} to add to the {@link MappingOperationModifier}
         * @param parameter1 first parameter {@link Value} for the {@link DualParameterOperation}
         * @param parameter2 second parameter {@link Value} for the {@link DualParameterOperation}
         * @param parameter3 third parameter {@link Value} for the {@link DualParameterOperation}
         * @return this builder
         */
        public Builder operation(DualParameterOperation operation, Value parameter1, Value parameter2, Value parameter3) {
            checkSpecialValue(parameter1);
            checkSpecialValue(parameter2);
            checkSpecialValue(parameter3);
            operationBuilder.operation(operation);
            parameters.addAll(List.of(parameter1, parameter2, parameter3));
            return this;
        }

        /**
         * @param operation {@link SourceOperation} to add to the {@link MappingOperationModifier}
         * @param parameters parameter {@link Value Values} for the {@link SourceOperation}
         * @return this builder
         */
        public Builder operation(Operation operation, Value ... parameters) {
            Arrays.stream(parameters).forEach(this::checkSpecialValue);
            if(parameters.length != operation.getParameterCount() + 1) {
                throw LogUtils.logExceptionAndGet(LoggerFactory.getLogger(Builder.class),
                        "invalid parameter count", IllegalArgumentException::new);
            }
            operationBuilder.operation(operation);
            this.parameters.addAll(Arrays.asList(parameters));
            return this;
        }

        /**
         * Clears all {@link Operation Operations} from the {@link MappingOperationModifier}.
         *
         * @return this builder
         */
        public Builder clearOperations() {
            operationBuilder.clearOperations();
            parameters.clear();
            return this;
        }

        /**
         * @return {@link MappingOperationModifier} initialized by this builder
         */
        public MappingOperationModifier build() {
            return new MappingOperationModifier(priority, operationBuilder.build(),
                    parameters.toArray(Value[]::new));
        }

        private void checkSpecialValue(Value parameter) {
            if(parameter instanceof SpecialValue) {
                SpecialNumeral sn = (SpecialNumeral) parameter.getValue();
                if(sn.getId() < -1 || sn.getId() >= parameters.size()) {
                    throw LogUtils.logExceptionAndGet(LoggerFactory.getLogger(Builder.class),
                            FUTURE_REFERENCE, IllegalArgumentException::new);
                }
            }
        }
    }

    /**
     * Extension of {@link StaticValue} that holds a {@link SpecialNumeral}. Use {@link #sourceValue()} and
     * {@link #resultValue(int)} to create instances of this.
     */
    public static class SpecialValue extends StaticValue {
        private SpecialValue(SpecialNumeral value) {
            super(value);
        }
    }
}
