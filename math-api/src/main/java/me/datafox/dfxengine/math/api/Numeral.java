package me.datafox.dfxengine.math.api;

import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A numeric value that can be backed by various {@link Number} types. Specifically, the allowed types are
 * {@link Integer}, {@link Long}, {@link BigInteger}, {@link Float}, {@link Double} {@link BigDecimal}. An
 * implementation of numeral should be immutable. It is called numeral to prevent name collisions with {@link Number}.
 *
 * @author datafox
 */
public interface Numeral extends Comparable<Numeral> {
    /**
     * @return the {@link Number} backing this numeral
     */
    Number getNumber();

    /**
     * @return the backing {@link Number}'s type
     */
    NumeralType getType();

    /**
     * @param type type to be checked for
     * @return {@code true} if this numeral can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but the value is not recognised as
     * any of the values of {@link NumeralType}. This should never happen
     */
    boolean canConvert(NumeralType type);

    /**
     * @param type type for this numeral to be converted to
     * @return a numeral backed with the specified type
     *
     * @throws ExtendedArithmeticException if the value of this numeral is outside the provided type's bounds
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but the value is not recognised as
     * any of the values of {@link NumeralType}. This should never happen
     */
    Numeral convert(NumeralType type);

    /**
     * @param type type for this numeral to be converted to
     * @return a numeral backed with the specified type, unless the value of this numeral is outside the specified
     * type's bounds, in which case this numeral is returned
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but the value is not recognised as
     * any of the values of {@link NumeralType}. This should never happen
     */
    Numeral convertIfAllowed(NumeralType type);

    /**
     * @return a numeral backed with the smallest integer type that can hold this numeral's value, unless this numeral
     * is already an integer, in which case this numeral is returned
     */
    Numeral toInteger();

    /**
     * @return a numeral backed with the smallest decimal type that can hold this numeral's value, unless this numeral
     * is already a decimal, in which case this numeral is returned
     */
    Numeral toDecimal();

    /**
     * @return a numeral backed with the smallest type that can hold this numeral's value. This method does not convert
     * between integer and decimal types.
     */
    Numeral toSmallestType();

    /**
     * @return the value of this numeral as an {@code int}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Integer#MIN_VALUE} or greater
     * than {@link Integer#MAX_VALUE}
     */
    int intValue();

    /**
     * @return the value of this numeral as a {@code long}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Long#MIN_VALUE} or greater
     * than {@link Long#MAX_VALUE}
     */
    long longValue();

    /**
     * @return the value of this numeral as a {@link BigInteger}
     */
    BigInteger bigIntValue();

    /**
     * @return the value of this numeral as a {@code float}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Float#MAX_VALUE -Float.MAX_VALUE}
     * or greater than {@link Float#MAX_VALUE}
     */
    float floatValue();

    /**
     * @return the value of this numeral as a {@code double}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than
     * {@link Double#MAX_VALUE -Double.MAX_VALUE} or greater than {@link Double#MAX_VALUE}
     */
    double doubleValue();

    /**
     * @return the value of this numeral as a {@link BigDecimal}
     */
    BigDecimal bigDecValue();
}
