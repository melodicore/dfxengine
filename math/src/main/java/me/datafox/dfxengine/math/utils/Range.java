package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.internal.MathStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Various math operations for checking the ranges of {@link Numeral Numerals}.
 *
 * @author datafox
 */
public class Range {
    private static final Logger logger = LoggerFactory.getLogger(Range.class);

    /**
     * {@link BigInteger} representation of {@link Integer#MIN_VALUE}.
     */
    public static final BigInteger INT_MIN_INT = BigInteger.valueOf(Integer.MIN_VALUE);

    /**
     * {@link BigInteger} representation of {@link Integer#MAX_VALUE}.
     */
    public static final BigInteger INT_MAX_INT = BigInteger.valueOf(Integer.MAX_VALUE);

    /**
     * {@link BigInteger} representation of {@link Long#MIN_VALUE}.
     */
    public static final BigInteger INT_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);

    /**
     * {@link BigInteger} representation of {@link Long#MAX_VALUE}.
     */
    public static final BigInteger INT_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    /**
     * {@link BigDecimal} representation of {@link Integer#MIN_VALUE}.
     */
    public static final BigDecimal DEC_MIN_INT = new BigDecimal(INT_MIN_INT);

    /**
     * {@link BigDecimal} representation of {@link Integer#MAX_VALUE}.
     */
    public static final BigDecimal DEC_MAX_INT = new BigDecimal(INT_MAX_INT);

    /**
     * {@link BigDecimal} representation of {@link Long#MIN_VALUE}.
     */
    public static final BigDecimal DEC_MIN_LONG = new BigDecimal(INT_MIN_LONG);

    /**
     * {@link BigDecimal} representation of {@link Long#MAX_VALUE}.
     */
    public static final BigDecimal DEC_MAX_LONG = new BigDecimal(INT_MAX_LONG);

    /**
     * {@link BigDecimal} representation of {@link Float#MAX_VALUE -Float.MAX_VALUE}.
     */
    public static final BigDecimal DEC_MIN_FLOAT = BigDecimal.valueOf(-Float.MAX_VALUE);

    /**
     * {@link BigDecimal} representation of {@link Float#MAX_VALUE}.
     */
    public static final BigDecimal DEC_MAX_FLOAT = BigDecimal.valueOf(Float.MAX_VALUE);

    /**
     * {@link BigDecimal} representation of {@link Double#MAX_VALUE -Double.MAX_VALUE}.
     */
    public static final BigDecimal DEC_MIN_DOUBLE = BigDecimal.valueOf(-Double.MAX_VALUE);

    /**
     * {@link BigDecimal} representation of {@link Double#MAX_VALUE Double.MAX_VALUE}.
     */
    public static final BigDecimal DEC_MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);

    /**
     * {@link BigInteger} representation of {@link Float#MAX_VALUE -Float.MAX_VALUE}.
     */
    public static final BigInteger INT_MIN_FLOAT = DEC_MIN_FLOAT.toBigInteger();

    /**
     * {@link BigInteger} representation of {@link Float#MAX_VALUE}.
     */
    public static final BigInteger INT_MAX_FLOAT = DEC_MAX_FLOAT.toBigInteger();

    /**
     * {@link BigInteger} representation of {@link Double#MAX_VALUE -Double.MAX_VALUE}.
     */
    public static final BigInteger INT_MIN_DOUBLE = DEC_MIN_DOUBLE.toBigInteger();

    /**
     * {@link BigInteger} representation of {@link Double#MAX_VALUE Double.MAX_VALUE}.
     */
    public static final BigInteger INT_MAX_DOUBLE = DEC_MAX_DOUBLE.toBigInteger();

    /**
     * @param numeral {@link Numeral} to be checked
     * @param type type to be checked
     * @return {@code true} if the specified {@link Numeral} is outside the allowed range of the type
     *
     * @throws NullPointerException if the specified type is {@code null} or if the {@link Numeral} returns {@code null}
     * for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the specified type is not {@code null} or if the {@link Numeral} does not
     * return {@code null} for {@link Numeral#getType()}, but the value is not recognised as any of the elements of
     * {@link NumeralType}. This should never happen
     */
    public static boolean isOutOfRange(Numeral numeral, NumeralType type) {
        if(type == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(type) {
            case INT:
                return isOutOfIntRange(numeral);
            case LONG:
                return isOutOfLongRange(numeral);
            case FLOAT:
                return isOutOfFloatRange(numeral);
            case DOUBLE:
                return isOutOfDoubleRange(numeral);
            case BIG_INT:
            case BIG_DEC:
                return false;
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(type),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be checked
     * @return {@code true} if the specified {@link Numeral} is smaller than {@link Integer#MIN_VALUE} or greater than
     * {@link Integer#MAX_VALUE}
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static boolean isOutOfIntRange(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
                return false;
            case LONG:
                return isOutOfIntRange(numeral.longValue());
            case BIG_INT:
                return isOutOfIntRange(numeral.bigIntValue());
            case FLOAT:
                return isOutOfIntRange(numeral.floatValue());
            case DOUBLE:
                return isOutOfIntRange(numeral.doubleValue());
            case BIG_DEC:
                return isOutOfIntRange(numeral.bigDecValue());
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be checked
     * @return {@code true} if the specified {@link Numeral} is smaller than {@link Long#MIN_VALUE} or greater than
     * {@link Long#MAX_VALUE}
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static boolean isOutOfLongRange(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
            case LONG:
                return false;
            case BIG_INT:
                return isOutOfLongRange(numeral.bigIntValue());
            case FLOAT:
                return isOutOfLongRange(numeral.floatValue());
            case DOUBLE:
                return isOutOfLongRange(numeral.doubleValue());
            case BIG_DEC:
                return isOutOfLongRange(numeral.bigDecValue());
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be checked
     * @return {@code true} if the specified {@link Numeral} is smaller than {@link Float#MAX_VALUE -Float.MAX_VALUE}
     * or greater than {@link Float#MAX_VALUE}
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static boolean isOutOfFloatRange(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
            case LONG:
            case FLOAT:
                return false;
            case BIG_INT:
                return isOutOfFloatRange(numeral.bigIntValue());
            case DOUBLE:
                return isOutOfFloatRange(numeral.doubleValue());
            case BIG_DEC:
                return isOutOfFloatRange(numeral.bigDecValue());
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be checked
     * @return {@code true} if the specified {@link Numeral} is smaller than {@link Double#MAX_VALUE -Double.MAX_VALUE}
     * or greater than {@link Double#MAX_VALUE}
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the elements of {@link NumeralType}. This
     * should never happen
     */
    public static boolean isOutOfDoubleRange(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
                return false;
            case BIG_INT:
                return isOutOfDoubleRange(numeral.bigIntValue());
            case BIG_DEC:
                return isOutOfDoubleRange(numeral.bigDecValue());
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param l {@code long} to be checked
     * @return {@code true} if the {@code long} is smaller than {@link Integer#MIN_VALUE} or greater than
     * {@link Integer#MAX_VALUE}
     */
    public static boolean isOutOfIntRange(long l) {
        return (int) l != l;
    }

    /**
     * @param bi {@link BigInteger} to be checked
     * @return {@code true} if the {@link BigInteger} is smaller than {@link Integer#MIN_VALUE} or greater than
     * {@link Integer#MAX_VALUE}
     */
    public static boolean isOutOfIntRange(BigInteger bi) {
        return bi.compareTo(BigInteger.valueOf(bi.intValue())) != 0;
    }

    /**
     * @param f {@code float} to be checked
     * @return {@code true} if the {@code float} is smaller than {@link Integer#MIN_VALUE} or greater than
     * {@link Integer#MAX_VALUE}
     */
    public static boolean isOutOfIntRange(float f) {
        return f > Integer.MAX_VALUE || f < Integer.MIN_VALUE;
    }

    /**
     * @param d {@code double} to be checked
     * @return {@code true} if the {@code double} is smaller than {@link Integer#MIN_VALUE} or greater than
     * {@link Integer#MAX_VALUE}
     */
    public static boolean isOutOfIntRange(double d) {
        return d > Integer.MAX_VALUE || d < Integer.MIN_VALUE;
    }

    /**
     * @param bd {@link BigDecimal} to be checked
     * @return {@code true} if the {@link BigDecimal} is smaller than {@link Integer#MIN_VALUE} or greater than
     * {@link Integer#MAX_VALUE}
     */
    public static boolean isOutOfIntRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_INT) > 0 || bd.compareTo(DEC_MIN_INT) < 0;
    }

    /**
     * @param bi {@link BigInteger} to be checked
     * @return {@code true} if the {@link BigInteger} is smaller than {@link Long#MIN_VALUE} or greater than
     * {@link Long#MAX_VALUE}
     */
    public static boolean isOutOfLongRange(BigInteger bi) {
        return bi.compareTo(BigInteger.valueOf(bi.longValue())) != 0;
    }

    /**
     * @param f {@code float} to be checked
     * @return {@code true} if the {@code float} is smaller than {@link Long#MIN_VALUE} or greater than
     * {@link Long#MAX_VALUE}
     */
    public static boolean isOutOfLongRange(float f) {
        return f > Long.MAX_VALUE || f < Long.MIN_VALUE;
    }

    /**
     * @param d {@code double} to be checked
     * @return {@code true} if the {@code double} is smaller than {@link Long#MIN_VALUE} or greater than
     * {@link Long#MAX_VALUE}
     */
    public static boolean isOutOfLongRange(double d) {
        return d > Long.MAX_VALUE || d < Long.MIN_VALUE;
    }

    /**
     * @param bd {@link BigDecimal} to be checked
     * @return {@code true} if the {@link BigDecimal} is smaller than {@link Long#MIN_VALUE} or greater than
     * {@link Long#MAX_VALUE}
     */
    public static boolean isOutOfLongRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_LONG) > 0 || bd.compareTo(DEC_MIN_LONG) < 0;
    }

    /**
     * @param bi {@link BigInteger} to be checked
     * @return {@code true} if the {@link BigInteger} is smaller than {@link Float#MAX_VALUE -Float.MAX_VALUE} or
     * greater than {@link Float#MAX_VALUE}
     */
    public static boolean isOutOfFloatRange(BigInteger bi) {
        return bi.compareTo(INT_MAX_FLOAT) > 0 || bi.compareTo(INT_MIN_FLOAT) < 0;
    }

    /**
     * @param d {@code double} to be checked
     * @return {@code true} if the {@code double} is smaller than {@link Float#MAX_VALUE -Float.MAX_VALUE} or
     * greater than {@link Float#MAX_VALUE}
     */
    public static boolean isOutOfFloatRange(double d) {
        return d > Float.MAX_VALUE || d < -Float.MAX_VALUE;
    }

    /**
     * @param bd {@link BigDecimal} to be checked
     * @return {@code true} if the {@link BigDecimal} is smaller than {@link Float#MAX_VALUE -Float.MAX_VALUE} or
     * greater than {@link Float#MAX_VALUE}
     */
    public static boolean isOutOfFloatRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_FLOAT) > 0 || bd.compareTo(DEC_MIN_FLOAT) < 0;
    }

    /**
     * @param bi {@link BigInteger} to be checked
     * @return {@code true} if the {@link BigInteger} is smaller than {@link Double#MAX_VALUE -Double.MAX_VALUE} or
     * greater than {@link Double#MAX_VALUE}
     */
    public static boolean isOutOfDoubleRange(BigInteger bi) {
        return bi.compareTo(INT_MAX_DOUBLE) > 0 || bi.compareTo(INT_MIN_DOUBLE) < 0;
    }

    /**
     * @param bd {@link BigDecimal} to be checked
     * @return {@code true} if the {@link BigDecimal} is smaller than {@link Double#MAX_VALUE -Double.MAX_VALUE} or
     * greater than {@link Double#MAX_VALUE}
     */
    public static boolean isOutOfDoubleRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_DOUBLE) > 0 || bd.compareTo(DEC_MIN_DOUBLE) < 0;
    }
}
