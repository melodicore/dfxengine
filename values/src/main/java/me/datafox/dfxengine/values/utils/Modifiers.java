package me.datafox.dfxengine.values.utils;

import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import static me.datafox.dfxengine.values.modifier.MappingOperationModifier.*;

/**
 * @author datafox
 */
public class Modifiers {

    public static OperationModifier operation(int priority, Operation operation, Value ... parameters) {
        return new OperationModifier(priority, operation, parameters);
    }

    public static OperationModifier operation(int priority, SourceOperation operation) {
        return new OperationModifier(priority, operation);
    }

    public static OperationModifier operation(int priority, SingleParameterOperation operation, Value parameter) {
        return new OperationModifier(priority, operation, parameter);
    }

    public static OperationModifier operation(int priority, DualParameterOperation operation, Value parameter1, Value parameter2) {
        return new OperationModifier(priority, operation, parameter1, parameter2);
    }

    public static OperationModifier add(int priority, Value addend) {
        return operation(priority, Operations::add, addend);
    }

    public static OperationModifier subtract(int priority, Value subtrahend) {
        return operation(priority, Operations::subtract, subtrahend);
    }

    public static OperationModifier subtractReversed(int priority, Value minuend) {
        return operation(priority, reverse(Operations::subtract), minuend);
    }

    public static OperationModifier multiply(int priority, Value multiplier) {
        return operation(priority, Operations::multiply, multiplier);
    }

    public static OperationModifier divide(int priority, Value divisor) {
        return operation(priority, Operations::divide, divisor);
    }

    public static OperationModifier divideReversed(int priority, Value dividend) {
        return operation(priority, reverse(Operations::divide), dividend);
    }

    public static OperationModifier inverse(int priority) {
        return operation(priority, Operations::inverse);
    }

    public static OperationModifier power(int priority, Value exponent) {
        return operation(priority, Operations::power, exponent);
    }

    public static OperationModifier powerReversed(int priority, Value base) {
        return operation(priority, reverse(Operations::power), base);
    }

    public static OperationModifier exp(int priority) {
        return operation(priority, Operations::exp);
    }

    public static OperationModifier sqrt(int priority) {
        return operation(priority, Operations::sqrt);
    }

    public static OperationModifier cbrt(int priority) {
        return operation(priority, Operations::cbrt);
    }

    public static OperationModifier root(int priority, Value root) {
        return operation(priority, Operations::root, root);
    }

    public static OperationModifier rootReversed(int priority, Value value) {
        return operation(priority, reverse(Operations::root), value);
    }

    public static OperationModifier log(int priority) {
        return operation(priority, Operations::log);
    }

    public static OperationModifier log2(int priority) {
        return operation(priority, Operations::log2);
    }

    public static OperationModifier log10(int priority) {
        return operation(priority, Operations::log10);
    }

    public static OperationModifier logN(int priority, Value base) {
        return operation(priority, Operations::logN, base);
    }

    public static OperationModifier logNReversed(int priority, Value value) {
        return operation(priority, reverse(Operations::logN), value);
    }

    public static OperationModifier min(int priority, Value value) {
        return operation(priority, Operations::min, value);
    }

    public static OperationModifier max(int priority, Value value) {
        return operation(priority, Operations::max, value);
    }

    public static OperationModifier lerp(int priority, Value min, Value max) {
        return operation(priority, Operations::lerp, min, max);
    }

    public static OperationModifier percentMultiply(int priority, Value percentage, Value multiplier) {
        return builder(priority)
                .operation(Operations::multiply,
                        percentage, multiplier)
                .operation(Operations::add,
                        resultValue(0), StaticValue.of(1))
                .operation(Operations::multiply,
                        sourceValue(), resultValue(1))
                .build();
    }

    public static OperationModifier powerMultiply(int priority, Value base, Value exponent) {
        return builder(priority)
                .operation(Operations::power, base, exponent)
                .operation(Operations::multiply, sourceValue(), resultValue(1))
                .build();
    }

    public static SingleParameterOperation reverse(SingleParameterOperation operation) {
        return (source, parameter) -> operation.apply(parameter, source);
    }
}
