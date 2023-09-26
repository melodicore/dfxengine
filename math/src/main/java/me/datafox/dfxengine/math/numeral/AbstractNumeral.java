package me.datafox.dfxengine.math.numeral;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.utils.Conversion;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Range;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.utils.Numerals.compare;

/**
 * A numeric value that can be backed by various {@link Number} types. Specifically, the allowed types are
 * {@link Integer}, {@link Long}, {@link BigInteger}, {@link Float}, {@link Double} {@link BigDecimal}. An
 * implementation of numeral should be immutable. It is called numeral to prevent name collisions with {@link Number}.
 *
 * @author datafox
 */

abstract class AbstractNumeral implements Numeral {
    /**
     * @param type type for this numeral to be converted to
     * @return a numeral backed with the specified type
     *
     * @throws ExtendedArithmeticException if the value of this numeral is outside the provided type's bounds
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but the value is not recognised as
     * any of the values of {@link NumeralType}. This should never happen
     */
    @Override
    public Numeral convert(NumeralType type) {
        return Conversion.toNumeral(this, type);
    }

    /**
     * @param type type for this numeral to be converted to
     * @return a numeral backed with the specified type, unless the value of this numeral is outside the specified
     * type's bounds, in which case this numeral is returned
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but the value is not recognised as
     * any of the values of {@link NumeralType}. This should never happen
     */
    @Override
    public Numeral convertIfAllowed(NumeralType type) {
        if(canConvert(type)) {
            return convert(type);
        }

        return this;
    }

    /**
     * @return a numeral backed with the smallest integer type that can hold this numeral's value, unless this numeral
     * is already an integer, in which case this numeral is returned
     */
    @Override
    public Numeral convertToInteger() {
        return Conversion.toInteger(this);
    }

    /**
     * @return a numeral backed with the smallest decimal type that can hold this numeral's value, unless this numeral
     * is already a decimal, in which case this numeral is returned
     */
    @Override
    public Numeral convertToDecimal() {
        return Conversion.toDecimal(this);
    }

    /**
     * @param type type to be checked for
     * @return {@code true} if this numeral can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but the value is not recognised as
     * any of the values of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean canConvert(NumeralType type) {
        return !Range.isOutOfRange(this, type);
    }

    /**
     * @return a numeral backed with the smallest type that can hold this numeral's value. This method does not convert
     * between integer and decimal types.
     */
    @Override
    public Numeral toSmallestType() {
        return Conversion.toSmallestType(this);
    }

    /**
     * @return the value of this numeral as an {@code int}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Integer#MIN_VALUE} or
     * greater than {@link Integer#MAX_VALUE}
     */
    @Override
    public int intValue() {
        return Conversion.toInt(this);
    }

    /**
     * @return the value of this numeral as a {@code long}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Long#MIN_VALUE} or
     * greater than {@link Long#MAX_VALUE}
     */
    @Override
    public long longValue() {
        return Conversion.toLong(this);
    }

    /**
     * @return the value of this numeral as a {@link BigInteger}
     */
    @Override
    public BigInteger bigIntValue() {
        return Conversion.toBigInt(this);
    }

    /**
     * @return the value of this numeral as a {@code float}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than
     * {@link Float#MAX_VALUE -Float.MAX_VALUE} or greater than {@link Float#MAX_VALUE}
     */
    @Override
    public float floatValue() {
        return Conversion.toFloat(this);
    }

    /**
     * @return the value of this numeral as a {@code double}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than
     * {@link Double#MAX_VALUE -Double.MAX_VALUE} or greater than {@link Double#MAX_VALUE}
     */
    @Override
    public double doubleValue() {
        return Conversion.toDouble(this);
    }

    /**
     * @return the value of this numeral as a {@link BigDecimal}
     */
    @Override
    public BigDecimal bigDecValue() {
        return Conversion.toBigDec(this);
    }

    /**
     * @return {@link String} representation of this numeral in format <i>Type(value)</i>
     */
    @Override
    public String toString() {
        return String.format("%s(%s)",
                getClass().getSimpleName().replace("Numeral", ""),
                getNumber());
    }

    /**
     * Compares this numeral with the specified numeral for order. The numerals are compared using
     * {@link Numerals#compare(Numeral, Numeral)}. Returns a negative  integer, zero, or a positive integer as this
     * numeral is less than, equal to, or greater than the specified numeral.
     *
     * @param other the numeral to be compared
     * @return a negative integer, zero, or a positive integer as this numeral is less than, equal to, or greater than
     * the specified numeral
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    @Override
    public int compareTo(Numeral other) {
        return compare(this, other);
    }
}
