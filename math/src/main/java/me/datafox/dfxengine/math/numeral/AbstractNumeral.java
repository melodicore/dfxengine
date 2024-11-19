package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.utils.Conversion;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Range;
import me.datafox.dfxengine.math.utils.internal.MathStrings;
import org.slf4j.Logger;

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

@EqualsAndHashCode
abstract class AbstractNumeral implements Numeral {
    private final NumeralType type;

    private final Logger logger;

    /**
     * Package-private constructor for {@link AbstractNumeral}.
     *
     * @param type type of the numeral
     */
    AbstractNumeral(NumeralType type) {
        this.type = type;
        logger = getLogger();
    }

    /**
     * Returns the type of the backing {@link Number}
     *
     * @return type of the backing {@link Number}
     */
    @Override
    public NumeralType getType() {
        return type;
    }

    /**
     * Checks if this numeral can be converted to the specified type.
     *
     * @param type type to be checked for
     * @return {@code true} if this numeral can be converted to the specified type
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public boolean canConvert(NumeralType type) {
        return !Range.isOutOfRange(this, type);
    }

    /**
     * Converts this numeral to the specified type, or throws an exception if the value is outside the specified type's
     * bounds.
     *
     * @param type type for this numeral to be converted to
     * @return numeral backed with the specified type
     *
     * @throws ExtendedArithmeticException if the value of this numeral is outside the specified type's bounds
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public Numeral convert(NumeralType type) {
        return Conversion.toNumeral(this, type);
    }

    /**
     * Converts this numeral to the specified type, or returns itself if the value is outside the specified type's
     * bounds.
     *
     * @param type type for this numeral to be converted to
     * @return numeral backed with the specified type, unless the value of this numeral is outside the specified type's
     * bounds, in which case this numeral is returned
     *
     * @throws NullPointerException if the specified type is {@code null}
     * @throws IllegalArgumentException if the specified type is not {@code null}, but it is not recognised as any of
     * the elements of {@link NumeralType}. This should never happen
     */
    @Override
    public Numeral convertIfAllowed(NumeralType type) {
        if(canConvert(type)) {
            return convert(type);
        }

        logger.info(MathStrings.couldNotConvert(this, type));

        return this;
    }

    /**
     * Converts this numeral to the smallest integer type that can hold this numeral's value, or returns itself if it is
     * already an integer.
     *
     * @return numeral backed with the smallest integer type that can hold this numeral's value, unless this numeral is
     * already an integer, in which case this numeral is returned
     */
    @Override
    public Numeral toInteger() {
        return Conversion.toInteger(this);
    }

    /**
     * Converts this numeral to the smallest decimal type that can hold this numeral's value, or returns itself if it is
     * already a decimal.
     *
     * @return numeral backed with the smallest decimal type that can hold this numeral's value, unless this numeral is
     * already a decimal, in which case this numeral is returned
     */
    @Override
    public Numeral toDecimal() {
        return Conversion.toDecimal(this);
    }

    /**
     * Converts this numeral to the smallest type that can hold this numeral's value. his method does not convert
     * between integer and decimal types.
     *
     * @return numeral backed with the smallest type that can hold this numeral's value.
     */
    @Override
    public Numeral toSmallestType() {
        return Conversion.toSmallestType(this);
    }

    /**
     * Returns the value of this numeral as an {@code int}.
     *
     * @return value of this numeral as an {@code int}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Integer#MIN_VALUE} or greater
     * than {@link Integer#MAX_VALUE}
     */
    @Override
    public int intValue() {
        return Conversion.toInt(this);
    }

    /**
     * Returns the value of this numeral as a {@code long}.
     *
     * @return value of this numeral as a {@code long}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Long#MIN_VALUE} or greater
     * than {@link Long#MAX_VALUE}
     */
    @Override
    public long longValue() {
        return Conversion.toLong(this);
    }

    /**
     * Returns the value of this numeral as a {@link BigInteger}.
     *
     * @return value of this numeral as a {@link BigInteger}
     */
    @Override
    public BigInteger bigIntValue() {
        return Conversion.toBigInt(this);
    }

    /**
     * Returns the value of this numeral as a {@code float}.
     *
     * @return value of this numeral as a {@code float}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than {@link Float#MAX_VALUE -Float.MAX_VALUE}
     * or greater than {@link Float#MAX_VALUE}
     */
    @Override
    public float floatValue() {
        return Conversion.toFloat(this);
    }

    /**
     * Returns the value of this numeral as a {@code double}.
     *
     * @return value of this numeral as a {@code double}
     *
     * @throws ExtendedArithmeticException if the value of this numeral is smaller than
     * {@link Double#MAX_VALUE -Double.MAX_VALUE} or greater than {@link Double#MAX_VALUE}
     */
    @Override
    public double doubleValue() {
        return Conversion.toDouble(this);
    }

    /**
     * Returns the value of this numeral as a {@link BigDecimal}.
     *
     * @return value of this numeral as a {@link BigDecimal}
     */
    @Override
    public BigDecimal bigDecValue() {
        return Conversion.toBigDec(this);
    }

    /**
     * Returns the {@link String} representation of this numeral in format <i>Type(value)</i>.
     *
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

    /**
     * Returns the {@link Logger} for this numeral.
     *
     * @return {@link Logger} for this numeral
     */
    protected abstract Logger getLogger();
}
