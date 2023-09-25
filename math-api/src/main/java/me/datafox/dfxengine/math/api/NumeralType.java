package me.datafox.dfxengine.math.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@link Enum} containing the six types that a {@link Numeral} can be backed with.
 *
 * @author datafox
 */
public enum NumeralType {
    /**
     * Value that represents an {@code int} or {@link Integer}.
     */
    INT(true, 0),

    /**
     * Value that represents a {@code long} or {@link Long}.
     */
    LONG(true, 1),

    /**
     * Value that represents a {@link BigInteger}.
     */
    BIG_INT(true, 2),

    /**
     * Value that represents a {@code float} or {@link Float}.
     */
    FLOAT(false, 3),

    /**
     * Value that represents a {@code double} or {@link Double}.
     */
    DOUBLE(false, 4),

    /**
     * Value that represents a {@link BigDecimal}.
     */
    BIG_DEC(false, 5);

    private final boolean integer;
    private final int significance;

    NumeralType(boolean integer, int significance) {
        this.integer = integer;
        this.significance = significance;
    }

    /**
     * @return {@code true} if this value represents an integer type ({@code int}, {@code long}, {@link BigInteger})
     */
    public boolean isInteger() {
        return integer;
    }

    /**
     * @return {@code true} if this value represents a decimal type ({@code float}, {@code double}, {@link BigDecimal})
     */
    public boolean isDecimal() {
        return !integer;
    }

    /**
     * @return the significance of this value
     */
    public int getSignificance() {
        return significance;
    }
}
