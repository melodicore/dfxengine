package me.datafox.dfxengine.math.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Enum containing the six types that a {@link Numeral} can be backed with.
 *
 * @author datafox
 */
public enum NumeralType {
    /**
     * Value that represents an int or {@link Integer}.
     */
    INT(true),

    /**
     * Value that represents a long or {@link Long}.
     */
    LONG(true),

    /**
     * Value that represents a {@link BigInteger}.
     */
    BIG_INT(true),

    /**
     * Value that represents a float or {@link Float}.
     */
    FLOAT(false),

    /**
     * Value that represents a double or {@link Double}.
     */
    DOUBLE(false),

    /**
     * Value that represents a {@link BigDecimal}.
     */
    BIG_DEC(false);

    private final boolean integer;

    NumeralType(boolean integer) {
        this.integer = integer;
    }

    /**
     * @return true if this value represents an integer type (int, long, {@link BigInteger})
     */
    public boolean isInteger() {
        return integer;
    }

    /**
     * @return true if this value represents a decimal type (float, double, {@link BigDecimal})
     */
    public boolean isDecimal() {
        return !integer;
    }
}
