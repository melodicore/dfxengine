package me.datafox.dfxengine.values.modifier;

import me.datafox.dfxengine.utils.LogUtils;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import me.datafox.dfxengine.values.operation.MappingOperationChain;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author datafox
 */
public class MappingOperationModifier extends OperationModifier {
    public MappingOperationModifier(int priority, MappingOperationChain operation, Value ... parameters) {
        super(LoggerFactory.getLogger(MappingOperationModifier.class), priority, operation, parameters);
    }

    public static Builder builder(int priority) {
        return new Builder(priority);
    }

    public static SpecialValue resultValue(int operationIndex) {
        return new SpecialValue(MappingOperationChain.resultNumeral(operationIndex));
    }

    public static SpecialValue sourceValue() {
        return new SpecialValue(MappingOperationChain.sourceNumeral());
    }

    public static class Builder {
        private final int priority;
        private final MappingOperationChain.Builder operationBuilder;
        private final List<Value> parameters;

        private Builder(int priority) {
            this.priority = priority;
            operationBuilder = MappingOperationChain.builder();
            parameters = new ArrayList<>();
        }

        public MappingOperationModifier.Builder operation(SourceOperation operation, Value parameter) {
            operationBuilder.operation(operation);
            parameters.add(parameter);
            return this;
        }

        public MappingOperationModifier.Builder operation(SingleParameterOperation operation, Value parameter1, Value parameter2) {
            operationBuilder.operation(operation);
            parameters.addAll(List.of(parameter1, parameter2));
            return this;
        }

        public MappingOperationModifier.Builder operation(DualParameterOperation operation, Value parameter1, Value parameter2, Value parameter3) {
            operationBuilder.operation(operation);
            parameters.addAll(List.of(parameter1, parameter2, parameter3));
            return this;
        }

        public MappingOperationModifier.Builder operation(Operation operation, Value ... parameters) {
            if(parameters.length != operation.getParameterCount() + 1) {
                throw LogUtils.logExceptionAndGet(LoggerFactory.getLogger(Builder.class),
                        "invalid parameter count", IllegalArgumentException::new);
            }
            operationBuilder.operation(operation);
            this.parameters.addAll(Arrays.asList(parameters));
            return this;
        }

        public MappingOperationModifier.Builder clearOperations() {
            operationBuilder.clearOperations();
            parameters.clear();
            return this;
        }

        public MappingOperationModifier build() {
            return new MappingOperationModifier(priority, operationBuilder.build(),
                    parameters.toArray(Value[]::new));
        }
    }

    public static class SpecialValue extends StaticValue {
        private SpecialValue(MappingOperationChain.SpecialNumeral value) {
            super(value);
        }
    }
}
