package me.datafox.dfxengine.math.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A numeric value that can be backed by various {@link Number} types. Specifically, the allowed types are
 * {@link Integer}, {@link Long}, {@link BigInteger}, {@link Float}, {@link Double} {@link BigDecimal}. An
 * implementation of Numeral should be immutable. It is called Numeral to prevent collisions with {@link Number}.
 *
 * @author datafox
 */
public interface Numeral extends Comparable<Numeral> {
    /**
     * @return the {@link Number} backing this Numeral
     */
    Number getNumber();

    /**
     * @return the backing {@link Number}'s type
     */
    NumeralType getType();

    /**
     * @param type type for this Numeral to be converted to
     * @return a Numeral backed with the specified type
     *
     * @throws ArithmeticException if the value of this Numeral is outside the provided type's bounds
     */
    Numeral convert(NumeralType type);

    /**
     * @param type type for this Numeral to be converted to
     * @return a Numeral backed with the specified type, unless the value of this Numeral is outside the specified
     * type's bounds, in which case this Numeral is returned
     */
    Numeral convertIfAllowed(NumeralType type);

    /**
     * @return a Numeral backed with the smallest integer type that can hold this Numeral's value, unless this Numeral
     * is already an integer, in which case this Numeral is returned
     */
    Numeral convertToInteger();

    /**
     * @return a Numeral backed with the smallest decimal type that can hold this Numeral's value, unless this Numeral
     * is already a decimal, in which case this Numeral is returned
     */
    Numeral convertToDecimal();

    /**
     * @param type type to be checked for
     * @return true if this Numeral can be converted to the specified type
     */
    boolean canConvert(NumeralType type);

    /**
     * @return a Numeral backed with the smallest type that can hold this Numeral's value. This method does not convert
     * between integer and decimal types.
     */
    Numeral toSmallestType();

    /**
     * @return the value of this Numeral as an int
     *
     * @throws ArithmeticException if the value of this Numeral is smaller than {@link Integer#MIN_VALUE} or greater
     * than {@link Integer#MAX_VALUE}
     */
    int intValue();

    /**
     * @return the value of this Numeral as a long
     *
     * @throws ArithmeticException if the value of this Numeral is smaller than {@link Long#MIN_VALUE} or greater
     * than {@link Long#MAX_VALUE}
     */
    long longValue();

    /**
     * @return the value of this Numeral as a {@link BigInteger}
     */
    BigInteger bigIntValue();

    /**
     * @return the value of this Numeral as a float
     *
     * @throws ArithmeticException if the value of this Numeral is smaller than {@link Float#MAX_VALUE -Float.MAX_VALUE}
     * or greater than {@link Float#MAX_VALUE}
     */
    float floatValue();

    /**
     * @return the value of this Numeral as a double
     *
     * @throws ArithmeticException if the value of this Numeral is smaller than
     * {@link Double#MAX_VALUE -Double.MAX_VALUE} or greater than {@link Double#MAX_VALUE}
     */
    double doubleValue();

    /**
     * @return the value of this Numeral as a {@link BigDecimal}
     */
    BigDecimal bigDecValue();
}
