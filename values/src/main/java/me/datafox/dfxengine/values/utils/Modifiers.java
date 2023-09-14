package me.datafox.dfxengine.values.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.operation.MappingOperationChain;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import java.util.List;

/**
 * @author datafox
 */
public class Modifiers {
    public static final SpecialValue SOURCE_VALUE = new SpecialValue(MappingOperationChain.SOURCE_NUMERAL);
    public static final SpecialValue RESULT_VALUE = new SpecialValue(MappingOperationChain.RESULT_NUMERAL);

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

    public static OperationModifier inverse(int priority) {
        return new OperationModifier(priority, Operations::inverse);
    }

    public static OperationModifier power(int priority, Value exponent) {
        return new OperationModifier(priority, Operations::power, exponent);
    }

    public static OperationModifier powerReversed(int priority, Value base) {
        return new OperationModifier(priority, reverse(Operations::power), base);
    }

    public static OperationModifier exp(int priority) {
        return new OperationModifier(priority, Operations::exp);
    }

    public static OperationModifier sqrt(int priority) {
        return new OperationModifier(priority, Operations::sqrt);
    }

    public static OperationModifier cbrt(int priority) {
        return new OperationModifier(priority, Operations::cbrt);
    }

    public static OperationModifier root(int priority, Value root) {
        return new OperationModifier(priority, Operations::root, root);
    }

    public static OperationModifier rootReversed(int priority, Value value) {
        return new OperationModifier(priority, reverse(Operations::root), value);
    }

    public static OperationModifier log(int priority) {
        return new OperationModifier(priority, Operations::log);
    }

    public static OperationModifier log2(int priority) {
        return new OperationModifier(priority, Operations::log2);
    }

    public static OperationModifier log10(int priority) {
        return new OperationModifier(priority, Operations::log10);
    }

    public static OperationModifier logN(int priority, Value base) {
        return new OperationModifier(priority, Operations::logN, base);
    }

    public static OperationModifier logReversed(int priority, Value value) {
        return new OperationModifier(priority, reverse(Operations::logN), value);
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
                        RESULT_VALUE, new StaticValue(1),
                        //Multiply source with result
                        SOURCE_VALUE, RESULT_VALUE));
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
                        SOURCE_VALUE, RESULT_VALUE));
    }

    public static SingleParameterOperation reverse(SingleParameterOperation operation) {
        return (source, parameter) -> operation.apply(parameter, source);
    }

    public static class SpecialValue extends StaticValue {
        private SpecialValue(Numeral value) {
            super(value);
        }
    }
}
