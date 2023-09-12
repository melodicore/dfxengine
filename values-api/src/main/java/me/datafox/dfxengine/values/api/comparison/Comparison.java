package me.datafox.dfxengine.values.api.comparison;

import me.datafox.dfxengine.math.api.Numeral;

import java.util.Objects;

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
        return Objects::equals;
    }
}
