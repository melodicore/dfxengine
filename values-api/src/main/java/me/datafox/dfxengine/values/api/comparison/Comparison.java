package me.datafox.dfxengine.values.api.comparison;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.Value;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author datafox
 */
public interface Comparison {
    boolean compare(Numeral numeral, Numeral other);
    
    static Comparison greaterThan() {
        return (numeral, other) -> numeral.compareTo(other) > 0;
    }

    static Comparison greaterOrEqual() {
        return (numeral, other) -> numeral.compareTo(other) > -1;
    }

    static Comparison lesserThan() {
        return (numeral, other) -> numeral.compareTo(other) < 0;
    }

    static Comparison lesserOrEqual() {
        return (numeral, other) -> numeral.compareTo(other) < 1;
    }

    static Comparison equal() {
        return (numeral, other) -> numeral.compareTo(other) == 0;
    }

    static Comparison strictEqual() {
        return Objects::equals;
    }

    default Predicate<Numeral> predicate(Numeral other) {
        return numeral -> compare(numeral, other);
    }

    default Predicate<Value> predicate(ComparisonContext context, Numeral other) {
        return value -> value.compare(this, context, other);
    }
}
