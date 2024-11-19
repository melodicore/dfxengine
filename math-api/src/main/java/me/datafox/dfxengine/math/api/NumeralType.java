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
    INT(true),

    /**
     * Value that represents a {@code long} or {@link Long}.
     */
    LONG(true),

    /**
     * Value that represents a {@link BigInteger}.
     */
    BIG_INT(true),

    /**
     * Value that represents a {@code float} or {@link Float}.
     */
    FLOAT(false),

    /**
     * Value that represents a {@code double} or {@link Double}.
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
     * Returns {@code true} if this value represents an integer type ({@code int}, {@code long}, {@link BigInteger}).
     *
     * @return {@code true} if this value represents an integer type ({@code int}, {@code long}, {@link BigInteger})
     */
    public boolean isInteger() {
        return integer;
    }

    /**
     * Returns {@code true} if this value represents a decimal type ({@code float}, {@code double}, {@link BigDecimal}).
     *
     * @return {@code true} if this value represents a decimal type ({@code float}, {@code double}, {@link BigDecimal})
     */
    public boolean isDecimal() {
        return !integer;
    }
}
