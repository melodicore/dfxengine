package me.datafox.dfxengine.values.api.comparison;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.values.api.Value;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A Comparison represents and arbitrary comparison of two {@link Numeral Numerals}. Has default implementations of
 * {@link #greaterThan()}, {@link #greaterOrEqual()}, {@link #lesserThan()}, {@link #lesserOrEqual()}, {@link #equal()}
 * and {@link #strictEqual()}.
 *
 * @author datafox
 */
public interface Comparison {
    /**
     * @param numeral first {@link Numeral} to be compared
     * @param other second {@link Numeral} to be compared
     * @return result of the comparison
     */
    boolean compare(Numeral numeral, Numeral other);

    /**
     * @return comparison that returns {@code true} if the first {@link Numeral} is greater than the second Numeral
     */
    static Comparison greaterThan() {
        return (numeral, other) -> numeral.compareTo(other) > 0;
    }

    /**
     * @return comparison that returns {@code true} if the first {@link Numeral} is greater than or equal to the second
     * Numeral
     */
    static Comparison greaterOrEqual() {
        return (numeral, other) -> numeral.compareTo(other) > -1;
    }

    /**
     * @return comparison that returns {@code true} if the first {@link Numeral} is lesser than the second Numeral
     */
    static Comparison lesserThan() {
        return (numeral, other) -> numeral.compareTo(other) < 0;
    }

    /**
     * @return comparison that returns {@code true} if the first {@link Numeral} is lesser than or equal to the second
     * Numeral
     */
    static Comparison lesserOrEqual() {
        return (numeral, other) -> numeral.compareTo(other) < 1;
    }

    /**
     * @return comparison that returns {@code true} if the first {@link Numeral} is equal to the second Numeral
     */
    static Comparison equal() {
        return (numeral, other) -> numeral.compareTo(other) == 0;
    }

    /**
     * @return comparison that returns {@code true} if the first {@link Numeral} is strictly equal to the second
     * Numeral. That is, the Numerals do not only represent the same number, but are also of the same
     * {@link NumeralType}
     */
    static Comparison strictEqual() {
        return Objects::equals;
    }

    /**
     * @param other second {@link Numeral} to be compared
     * @return {@link Predicate} for {@link Numeral} that returns the result of this comparison against a second Numeral
     */
    default Predicate<Numeral> predicate(Numeral other) {
        return numeral -> compare(numeral, other);
    }

    /**
     * @param context {@link ComparisonContext} for this comparison
     * @param other second {@link Numeral} to be compared
     * @return {@link Predicate} for {@link Value} that returns the result of this comparison against a second Numeral
     * with the specified {@link ComparisonContext}
     */
    default Predicate<Value> predicate(ComparisonContext context, Numeral other) {
        return value -> value.compare(this, context, other);
    }
}
