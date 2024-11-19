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
     * Compares a {@link Numeral} to another.
     *
     * @param numeral first {@link Numeral} to be compared
     * @param other second {@link Numeral} to be compared
     * @return result of the comparison
     */
    boolean compare(Numeral numeral, Numeral other);

    /**
     * Returns a comparison that returns {@code true} if the first {@link Numeral} is greater than the second numeral.
     *
     * @return comparison that returns {@code true} if the first {@link Numeral} is greater than the second numeral
     */
    static Comparison greaterThan() {
        return (numeral, other) -> numeral.compareTo(other) > 0;
    }

    /**
     * Returns a comparison that returns {@code true} if the first {@link Numeral} is greater than or equal to the
     * second numeral.
     *
     * @return comparison that returns {@code true} if the first {@link Numeral} is greater than or equal to the second
     * numeral
     */
    static Comparison greaterOrEqual() {
        return (numeral, other) -> numeral.compareTo(other) > -1;
    }

    /**
     * Returns a comparison that returns {@code true} if the first {@link Numeral} is lesser than the second numeral.
     *
     * @return comparison that returns {@code true} if the first {@link Numeral} is lesser than the second numeral
     */
    static Comparison lesserThan() {
        return (numeral, other) -> numeral.compareTo(other) < 0;
    }

    /**
     * Returns a comparison that returns {@code true} if the first {@link Numeral} is lesser than or equal to the second
     * numeral.
     *
     * @return comparison that returns {@code true} if the first {@link Numeral} is lesser than or equal to the second
     * numeral
     */
    static Comparison lesserOrEqual() {
        return (numeral, other) -> numeral.compareTo(other) < 1;
    }

    /**
     * Returns a comparison that returns {@code true} if the first {@link Numeral} is equal to the second numeral.
     *
     * @return comparison that returns {@code true} if the first {@link Numeral} is equal to the second numeral
     */
    static Comparison equal() {
        return (numeral, other) -> numeral.compareTo(other) == 0;
    }

    /**
     * Returns a comparison that returns {@code true} if the first {@link Numeral} is strictly equal to the second
     * numeral. That is, the numerals do not only represent the same number, but are also of the same
     * {@link NumeralType}.
     *
     * @return comparison that returns {@code true} if the first {@link Numeral} is strictly equal to the second
     * numeral.
     */
    static Comparison strictEqual() {
        return Objects::equals;
    }

    /**
     * Returns a {@link Predicate} for {@link Numeral} that returns the result of this comparison against a second
     * numeral.
     *
     * @param other second {@link Numeral} to be compared
     * @return {@link Predicate} for {@link Numeral} that returns the result of this comparison against a second numeral
     */
    default Predicate<Numeral> predicate(Numeral other) {
        return numeral -> compare(numeral, other);
    }

    /**
     * Returns a {@link Predicate} for {@link Value} that returns the result of this comparison against a second numeral
     * with the specified {@link ComparisonContext}.
     *
     * @param context {@link ComparisonContext} for this comparison
     * @param other second {@link Numeral} to be compared
     * @return {@link Predicate} for {@link Value} that returns the result of this comparison against a second numeral
     * with the specified {@link ComparisonContext}
     */
    default Predicate<Value> predicate(ComparisonContext context, Numeral other) {
        return value -> value.compare(this, context, other);
    }
}
