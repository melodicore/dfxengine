package me.datafox.dfxengine.values.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;
import me.datafox.dfxengine.values.modifier.MappingOperationModifier;
import me.datafox.dfxengine.values.modifier.OperationModifier;

import static me.datafox.dfxengine.values.modifier.MappingOperationModifier.resultValue;
import static me.datafox.dfxengine.values.modifier.MappingOperationModifier.sourceValue;

/**
 * A utility class that contains various methods for creating {@link Modifier Modifiers}.
 *
 * @author datafox
 */
public class Modifiers {
    /**
     * @param priority priority for the {@link OperationModifier}
     * @param operation {@link Operation} for the {@link OperationModifier}
     * @param parameters parameter {@link Value Values} for the {@link OperationModifier}
     * @return {@link OperationModifier} with the specified parameters
     */
    public static OperationModifier operation(int priority, Operation operation, Value ... parameters) {
        return new OperationModifier(priority, operation, parameters);
    }

    /**
     * @param priority priority for the {@link OperationModifier}
     * @param operation {@link Operation} for the {@link OperationModifier}
     * @return {@link OperationModifier} with the specified parameters
     */
    public static OperationModifier operation(int priority, SourceOperation operation) {
        return new OperationModifier(priority, operation);
    }

    /**
     * @param priority priority for the {@link OperationModifier}
     * @param operation {@link Operation} for the {@link OperationModifier}
     * @param parameter parameter {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} with the specified parameters
     */
    public static OperationModifier operation(int priority, SingleParameterOperation operation, Value parameter) {
        return new OperationModifier(priority, operation, parameter);
    }

    /**
     * @param priority priority for the {@link OperationModifier}
     * @param operation {@link Operation} for the {@link OperationModifier}
     * @param parameter1 first parameter {@link Value} for the {@link OperationModifier}
     * @param parameter2 second parameter {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} with the specified parameters
     */
    public static OperationModifier operation(int priority, DualParameterOperation operation, Value parameter1, Value parameter2) {
        return new OperationModifier(priority, operation, parameter1, parameter2);
    }

    /**
     * Wrapper for {@link Operations#add(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param addend addend {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does addition
     */
    public static OperationModifier add(int priority, Value addend) {
        return operation(priority, Operations::add, addend);
    }

    /**
     * Wrapper for {@link Operations#subtract(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param subtrahend subtrahend {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does subtraction
     */
    public static OperationModifier subtract(int priority, Value subtrahend) {
        return operation(priority, Operations::subtract, subtrahend);
    }

    /**
     * Wrapper for {@link Operations#subtract(Numeral, Numeral)} with reversed parameters.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param minuend minuend {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does subtraction
     */
    public static OperationModifier subtractReversed(int priority, Value minuend) {
        return operation(priority, reverse(Operations::subtract), minuend);
    }

    /**
     * Wrapper for {@link Operations#multiply(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param multiplier multiplier {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does multiplication
     */
    public static OperationModifier multiply(int priority, Value multiplier) {
        return operation(priority, Operations::multiply, multiplier);
    }

    /**
     * Wrapper for {@link Operations#divide(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param divisor divisor {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does division
     */
    public static OperationModifier divide(int priority, Value divisor) {
        return operation(priority, Operations::divide, divisor);
    }

    /**
     * Wrapper for {@link Operations#divide(Numeral, Numeral)} with reversed parameters.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param dividend dividend {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does division
     */
    public static OperationModifier divideReversed(int priority, Value dividend) {
        return operation(priority, reverse(Operations::divide), dividend);
    }

    /**
     * Wrapper for {@link Operations#inverse(Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @return {@link OperationModifier} that does inversion
     */
    public static OperationModifier inverse(int priority) {
        return operation(priority, Operations::inverse);
    }

    /**
     * Wrapper for {@link Operations#power(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param exponent exponent {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does exponentiation
     */
    public static OperationModifier power(int priority, Value exponent) {
        return operation(priority, Operations::power, exponent);
    }

    /**
     * Wrapper for {@link Operations#power(Numeral, Numeral)} with reversed parameters.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param base base {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does exponentiation
     */
    public static OperationModifier powerReversed(int priority, Value base) {
        return operation(priority, reverse(Operations::power), base);
    }

    /**
     * Wrapper for {@link Operations#exp(Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @return {@link OperationModifier} that does exponentiation
     */
    public static OperationModifier exp(int priority) {
        return operation(priority, Operations::exp);
    }

    /**
     * Wrapper for {@link Operations#sqrt(Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @return {@link OperationModifier} that does square root
     */
    public static OperationModifier sqrt(int priority) {
        return operation(priority, Operations::sqrt);
    }

    /**
     * Wrapper for {@link Operations#cbrt(Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @return {@link OperationModifier} that does cube root
     */
    public static OperationModifier cbrt(int priority) {
        return operation(priority, Operations::cbrt);
    }

    /**
     * Wrapper for {@link Operations#root(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param base base {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does nth base
     */
    public static OperationModifier root(int priority, Value base) {
        return operation(priority, Operations::root, base);
    }

    /**
     * Wrapper for {@link Operations#root(Numeral, Numeral)} with reversed parameters.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param value value {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does nth root
     */
    public static OperationModifier rootReversed(int priority, Value value) {
        return operation(priority, reverse(Operations::root), value);
    }

    /**
     * Wrapper for {@link Operations#log(Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @return {@link OperationModifier} that does natural logarithm
     */
    public static OperationModifier log(int priority) {
        return operation(priority, Operations::log);
    }

    /**
     * Wrapper for {@link Operations#log2(Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @return {@link OperationModifier} that does binary logarithm
     */
    public static OperationModifier log2(int priority) {
        return operation(priority, Operations::log2);
    }

    /**
     * Wrapper for {@link Operations#log10(Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @return {@link OperationModifier} that does decimal logarithm
     */
    public static OperationModifier log10(int priority) {
        return operation(priority, Operations::log10);
    }

    /**
     * Wrapper for {@link Operations#root(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param base base {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does nth logarithm
     */
    public static OperationModifier logN(int priority, Value base) {
        return operation(priority, Operations::logN, base);
    }

    /**
     * Wrapper for {@link Operations#root(Numeral, Numeral)} with reversed parameters.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param value value {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does nth logarithm
     */
    public static OperationModifier logNReversed(int priority, Value value) {
        return operation(priority, reverse(Operations::logN), value);
    }

    /**
     * Wrapper for {@link Operations#min(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param value value {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does minimum
     */
    public static OperationModifier min(int priority, Value value) {
        return operation(priority, Operations::min, value);
    }

    /**
     * Wrapper for {@link Operations#max(Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param value value {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does maximum
     */
    public static OperationModifier max(int priority, Value value) {
        return operation(priority, Operations::max, value);
    }

    /**
     * Wrapper for {@link Operations#lerp(Numeral, Numeral, Numeral)}.
     *
     * @param priority priority for the {@link OperationModifier}
     * @param min minimum {@link Value} for the {@link OperationModifier}
     * @param max maximum {@link Value} for the {@link OperationModifier}
     * @return {@link OperationModifier} that does linear interpolation
     */
    public static OperationModifier lerp(int priority, Value min, Value max) {
        return operation(priority, Operations::lerp, min, max);
    }

    /**
     * The formula for the {@link MappingOperationModifier} is {@code [source] * ([percentage] * [multiplier] + 1)}.
     *
     * @param priority priority for the {@link MappingOperationModifier}
     * @param percentage percentage {@link Value} for the {@link MappingOperationModifier}
     * @param multiplier multiplier {@link Value} for the {@link MappingOperationModifier}
     * @return {@link MappingOperationModifier} that does the specified mathematical operation
     */
    public static MappingOperationModifier fractionMultiply(int priority, Value percentage, Value multiplier) {
        return MappingOperationModifier
                .builder(priority)
                .operation(Operations::multiply,
                        percentage, multiplier)
                .operation(Operations::add,
                        resultValue(0), Values.of(1))
                .operation(Operations::multiply,
                        sourceValue(), resultValue(1))
                .build();
    }

    /**
     * The formula for the {@link MappingOperationModifier} is <code>[source] * ([base]<sup>[exponent]</sup>)</code>.
     *
     * @param priority priority for the {@link MappingOperationModifier}
     * @param base base {@link Value} for the {@link MappingOperationModifier}
     * @param exponent exponent {@link Value} for the {@link MappingOperationModifier}
     * @return {@link MappingOperationModifier} that does the specified mathematical operation
     */
    public static MappingOperationModifier powerMultiply(int priority, Value base, Value exponent) {
        return MappingOperationModifier
                .builder(priority)
                .operation(Operations::power, base, exponent)
                .operation(Operations::multiply, sourceValue(), resultValue(1))
                .build();
    }

    /**
     * @param operation {@link SingleParameterOperation} to be reversed
     * @return {@link SingleParameterOperation} with reversed parameters
     */
    public static SingleParameterOperation reverse(SingleParameterOperation operation) {
        return (source, parameter) -> operation.apply(parameter, source);
    }
}
