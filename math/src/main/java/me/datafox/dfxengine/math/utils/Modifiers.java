package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Value;
import me.datafox.dfxengine.math.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.math.modifier.OperationModifier;
import me.datafox.dfxengine.math.operation.MappingOperationChain;
import me.datafox.dfxengine.math.value.StaticValue;

import java.util.List;

/**
 * @author datafox
 */
public class Modifiers {
    public static OperationModifier add(int priority, Value addend) {
        return new OperationModifier(priority, Operations::add, addend);
    }

    public static OperationModifier subtract(int priority, Value subtrahend) {
        return new OperationModifier(priority, Operations::subtract, subtrahend);
    }

    public static OperationModifier subtractReversed(int priority, Value minuend) {
        return new OperationModifier(priority, reverse(Operations::subtract), minuend);
    }

    public static OperationModifier multiply(int priority, Value multiplier) {
        return new OperationModifier(priority, Operations::multiply, multiplier);
    }

    public static OperationModifier divide(int priority, Value divisor) {
        return new OperationModifier(priority, Operations::divide, divisor);
    }

    public static OperationModifier divideReversed(int priority, Value dividend) {
        return new OperationModifier(priority, reverse(Operations::divide), dividend);
    }

    public static OperationModifier power(int priority, Value exponent) {
        return new OperationModifier(priority, Operations::power, exponent);
    }

    public static OperationModifier powerReversed(int priority, Value base) {
        return new OperationModifier(priority, reverse(Operations::power), base);
    }

    public static OperationModifier percentMultiply(int priority, Value percentage, Value multiplier) {
        return new OperationModifier(priority, MappingOperationChain.builder()
                .operation(Operations::multiply)
                .operation(Operations::add)
                .operation(Operations::multiply)
                .build(),
                List.of(
                        //Multiply percentage with multiplier
                        percentage, multiplier,
                        //Add 1 to result
                        MappingOperationChain.RESULT_VALUE, new StaticValue(1),
                        //Multiply source with result
                        MappingOperationChain.SOURCE_VALUE, MappingOperationChain.RESULT_VALUE));
    }

    public static OperationModifier powerMultiply(int priority, Value base, Value exponent) {
        return new OperationModifier(priority, MappingOperationChain.builder()
                .operation(Operations::power)
                .operation(Operations::multiply)
                .build(),
                List.of(
                        //Exponentiation
                        base, exponent,
                        //Multiply source with result
                        MappingOperationChain.SOURCE_VALUE, MappingOperationChain.RESULT_VALUE));
    }

    private static SingleParameterOperation reverse(SingleParameterOperation operation) {
        return (source, parameter) -> operation.apply(parameter, source);
    }
}
