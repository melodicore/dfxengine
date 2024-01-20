package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.numeral.*;
import me.datafox.dfxengine.math.utils.internal.MathStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static me.datafox.dfxengine.math.api.NumeralType.BIG_DEC;
import static me.datafox.dfxengine.math.api.NumeralType.BIG_INT;

/**
 * Various operations for {@link Numeral Numerals}.
 *
 * @author datafox
 */
public class Numerals {
    private static final Logger logger = LoggerFactory.getLogger(Numerals.class);

    /**
     * @param i {@code int} value
     * @return {@link IntNumeral} representing specified value
     */
    public static IntNumeral of(int i) {
        return new IntNumeral(i);
    }

    /**
     * @param l {@code long} value
     * @return {@link LongNumeral} representing specified value
     */
    public static LongNumeral of(long l) {
        return new LongNumeral(l);
    }

    /**
     * @param bi {@link BigInteger} value
     * @return {@link BigIntNumeral} representing specified value
     */
    public static BigIntNumeral of(BigInteger bi) {
        return new BigIntNumeral(bi);
    }

    /**
     * @param f {@code float} value
     * @return {@link FloatNumeral} representing specified value
     */
    public static FloatNumeral of(float f) {
        return new FloatNumeral(f);
    }

    /**
     * @param d {@code double} value
     * @return {@link DoubleNumeral} representing specified value
     */
    public static DoubleNumeral of(double d) {
        return new DoubleNumeral(d);
    }

    /**
     * @param bd {@link BigDecimal} value
     * @return {@link BigDecNumeral} representing specified value
     */
    public static BigDecNumeral of(BigDecimal bd) {
        return new BigDecNumeral(bd);
    }

    /**
     * If the specified {@link String} represents an integer, a {@link BigIntNumeral} is returned. Otherwise, a
     * {@link BigDecNumeral} is returned. A string is considered to represent an integer if it does not contain any of
     * the characters {@code .}, {@code e} or {@code E}.
     *
     * @param str {@link String} representation of a numeric value
     * @return {@link Numeral} representing specified value
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static Numeral of(String str) {
        if(str.matches(".*[.eE].*")) {
            return of(new BigDecimal(str));
        } else {
            return of(new BigInteger(str));
        }
    }

    /**
     * @param number {@link Number} value
     * @return {@link Numeral} representing the specified value
     *
     * @throws IllegalArgumentException if the {@link Number} is not any of the following classes: {@link Integer},
     * {@link Long}, {@link BigInteger}, {@link Float}, {@link Double} or {@link BigDecimal}
     */
    public static Numeral of(Number number) {
        if(number instanceof Integer) {
            return of(number.intValue());
        }
        if(number instanceof Long) {
            return of(number.longValue());
        }
        if(number instanceof BigInteger) {
            return of((BigInteger) number);
        }
        if(number instanceof Float) {
            return of(number.floatValue());
        }
        if(number instanceof Double) {
            return of(number.doubleValue());
        }
        if(number instanceof BigDecimal) {
            return of((BigDecimal) number);
        }
        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownNumberType(number),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be checked
     * @return {@code true} if the specified value represents the number zero
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static boolean isZero(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
                return numeral.intValue() == 0;
            case LONG:
                return numeral.longValue() == 0L;
            case BIG_INT:
                return numeral.bigIntValue().equals(BigInteger.ZERO);
            case FLOAT:
                return numeral.floatValue() == 0f;
            case DOUBLE:
                return numeral.doubleValue() == 0d;
            case BIG_DEC:
                return numeral.bigDecValue().compareTo(BigDecimal.ZERO) == 0;
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }


    /**
     * @param numeral {@link Numeral} to be checked
     * @return {@code true} if the specified value represents the number one
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static boolean isOne(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
                return numeral.intValue() == 1;
            case LONG:
                return numeral.longValue() == 1L;
            case BIG_INT:
                return numeral.bigIntValue().equals(BigInteger.ONE);
            case FLOAT:
                return numeral.floatValue() == 1f;
            case DOUBLE:
                return numeral.doubleValue() == 1d;
            case BIG_DEC:
                return numeral.bigDecValue().compareTo(BigDecimal.ONE) == 0;
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be checked
     * @return {@code true} if the {@link Numeral} represents an even number. For decimal Numerals, the number must not
     * have a decimal part for {@code true} to be returned;
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static boolean isEven(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
                return (numeral.intValue() & 1) == 0;
            case LONG:
                return (numeral.longValue() & 1) == 0;
            case BIG_INT:
                return !numeral.bigIntValue().testBit(0);
            case FLOAT:
                return numeral.floatValue() % 2 == 0;
            case DOUBLE:
                return numeral.doubleValue() % 2 == 0;
            case BIG_DEC:
                return isBigDecimalAnInteger(numeral.bigDecValue()) &&
                        !numeral.bigDecValue().toBigInteger().testBit(0);
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral first {@link Numeral} to compare
     * @param other second {@link Numeral} to compare
     * @return 0 if the {@link Numeral} values represent the same number, 1 if the first Numeral represents a larger
     * value than the second Numeral, and -1 if the first Numeral represents a smaller value than the second Numeral.
     *
     * @throws NullPointerException if any of the {@link Numeral Numerals} return {@code null} for
     * {@link Numeral#getType()}
     * @throws IllegalArgumentException if any of the {@link Numeral Numerals} do not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static int compare(Numeral numeral, Numeral other) {
        NumeralType type = getSignificantType(numeral.getType(), other.getType());

        switch(type) {
            case INT:
                return Integer.compare(numeral.intValue(), other.intValue());
            case LONG:
                return Long.compare(numeral.longValue(), other.longValue());
            case BIG_INT:
                return numeral.bigIntValue().compareTo(other.bigIntValue());
            case FLOAT:
                return Float.compare(numeral.floatValue(), other.floatValue());
            case DOUBLE:
                return Double.compare(numeral.doubleValue(), other.doubleValue());
            case BIG_DEC:
                return numeral.bigDecValue().compareTo(other.bigDecValue());
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(type),
                IllegalArgumentException::new);
    }

    /**
     * @param type1 first {@link NumeralType}
     * @param type2 second {@link NumeralType}
     * @return the most significant type of specified values
     *
     * @throws NullPointerException if any of the types are {@code null}
     *
     * @see Numerals#getSignificantType(NumeralType...)
     */
    public static NumeralType getSignificantType(NumeralType type1, NumeralType type2) {
        if(Objects.equals(type1, type2)) {
            return type1;
        }

        return getSignificantType(new NumeralType[] { type1, type2 });
    }

    /**
     * <p>
     * Returns the most significant {@link NumeralType}. The significance of a type is determined by how large numerical
     * values it can represent. If any of the specified types is a {@link NumeralType#isDecimal() decimal type}, the
     * return value of this method is also a decimal type. If any of the specified types is {@code null}, the return
     * value is also {@code null}. {@code null} check takes precedence over all other checks.
     * </p>
     * <p>
     * Therefore, if any of the specified types is {@link NumeralType#BIG_DEC BIG_DEC}, BIG_DEC is also returned.
     * BIG_DEC is also returned if any of the specified types is {@link NumeralType#BIG_INT BIG_INT} and another is
     * a decimal type. In all other cases the specified type with the highest
     * {@link NumeralType#getSignificance() significance} will be returned.
     * </p>
     *
     * @param types types to be checked
     * @return the most significant type
     *
     * @throws NullPointerException if any of the types are {@code null}
     */
    public static NumeralType getSignificantType(NumeralType ... types) {
        if(types.length == 0) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.EMPTY_ARRAY,
                    IllegalArgumentException::new);
        }

        for(NumeralType type : types) {
            if(type == null) {
                throw LogUtils.logExceptionAndGet(logger,
                        MathStrings.NULL_NUMBER_TYPE,
                        NullPointerException::new);
            }
        }

        NumeralType significantType = types[0];

        if(significantType.equals(BIG_DEC)) {
            return BIG_DEC;
        }

        for(int i=1;i<types.length;i++) {
            NumeralType type = types[i];

            if(significantType.equals(type)) {
                continue;
            }

            if(type.equals(BIG_DEC) ||
                    (type.isDecimal() && significantType.equals(BIG_INT)) ||
                    (significantType.isDecimal() && type.equals(BIG_INT))) {
                return BIG_DEC;
            }

            if(type.getSignificance() > significantType.getSignificance()) {
                significantType = type;
            }
        }

        return significantType;
    }

    private static boolean isBigDecimalAnInteger(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
    }
}
