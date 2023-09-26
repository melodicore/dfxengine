package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.numeral.*;
import me.datafox.dfxengine.math.utils.internal.MathStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.utils.Range.*;

/**
 * Various conversion methods for {@link Numeral Numerals}.
 *
 * @author datafox
 */
public class Conversion {
    private static final Logger logger = LoggerFactory.getLogger(Conversion.class);

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@code int} representation of the specified value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Integer#MIN_VALUE} or greater than {@link Integer#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static int toInt(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral instanceof IntNumeral) {
            return numeral.intValue();
        }

        try {
            switch(numeral.getType()) {
                case INT:
                    return numeral.getNumber().intValue();
                case LONG:
                    return Math.toIntExact(numeral.getNumber().longValue());
                case BIG_INT:
                    return ((BigInteger) numeral.getNumber()).intValueExact();
                case FLOAT:
                    return toIntInRange(numeral.getNumber().floatValue());
                case DOUBLE:
                    return toIntInRange(numeral.getNumber().doubleValue());
                case BIG_DEC:
                    return toIntInRange((BigDecimal) numeral.getNumber());
            }

            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.unknownType(numeral.getType()),
                    IllegalArgumentException::new);
        } catch(ArithmeticException e) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.duringIntConversion(numeral),
                    e, ExtendedArithmeticException::new);
        }
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@code long} representation of the specified value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Long#MIN_VALUE} or greater than {@link Long#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static long toLong(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral instanceof LongNumeral) {
            return numeral.longValue();
        }

        try {
            switch(numeral.getType()) {
                case INT:
                case LONG:
                    return numeral.getNumber().longValue();
                case BIG_INT:
                    return ((BigInteger) numeral.getNumber()).longValueExact();
                case FLOAT:
                    return toLongInRange(numeral.getNumber().floatValue());
                case DOUBLE:
                    return toLongInRange(numeral.getNumber().doubleValue());
                case BIG_DEC:
                    return toLongInRange((BigDecimal) numeral.getNumber());
            }
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.unknownType(numeral.getType()),
                    IllegalArgumentException::new);

        } catch(ArithmeticException e) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.duringLongConversion(numeral),
                    e, ExtendedArithmeticException::new);
        }
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link BigInteger} representation of the specified value
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static BigInteger toBigInt(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral instanceof BigIntNumeral || numeral instanceof FloatNumeral) {
            return numeral.bigIntValue();
        }

        switch(numeral.getType()) {
            case INT:
                return BigInteger.valueOf(numeral.getNumber().intValue());
            case LONG:
                return BigInteger.valueOf(numeral.getNumber().longValue());
            case BIG_INT:
                return (BigInteger) numeral.getNumber();
            case FLOAT:
                return BigDecimal.valueOf(numeral.getNumber().floatValue()).toBigInteger();
            case DOUBLE:
                return BigDecimal.valueOf(numeral.getNumber().doubleValue()).toBigInteger();
            case BIG_DEC:
                return ((BigDecimal) numeral.getNumber()).toBigInteger();
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@code float} representation of the specified value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Float#MAX_VALUE -Float.MAX_VALUE} or greater than {@link Float#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static float toFloat(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral instanceof FloatNumeral) {
            return numeral.floatValue();
        }

        try {
            switch(numeral.getType()) {
                case INT:
                case LONG:
                case FLOAT:
                    return numeral.getNumber().floatValue();
                case BIG_INT:
                    return toFloatInRange((BigInteger) numeral.getNumber());
                case DOUBLE:
                    return toFloatInRange(numeral.getNumber().doubleValue());
                case BIG_DEC:
                    return toFloatInRange((BigDecimal) numeral.getNumber());
            }

            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.unknownType(numeral.getType()),
                    IllegalArgumentException::new);
        } catch(ArithmeticException e) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.duringFloatConversion(numeral),
                    e, ExtendedArithmeticException::new);
        }
    }


    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@code double} representation of the specified value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Double#MAX_VALUE -Double.MAX_VALUE} or greater than {@link Double#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static double toDouble(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral instanceof DoubleNumeral || numeral instanceof FloatNumeral) {
            return numeral.doubleValue();
        }

        try {
            switch(numeral.getType()) {
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                    return numeral.getNumber().doubleValue();
                case BIG_INT:
                    return toDoubleInRange((BigInteger) numeral.getNumber());
                case BIG_DEC:
                    return toDoubleInRange((BigDecimal) numeral.getNumber());
            }

            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.unknownType(numeral.getType()),
                    IllegalArgumentException::new);
        } catch(ArithmeticException e) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.duringDoubleConversion(numeral),
                    e, ExtendedArithmeticException::new);
        }
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link BigDecimal} representation of the specified value
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static BigDecimal toBigDec(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral instanceof BigDecNumeral || numeral instanceof FloatNumeral) {
            return numeral.bigDecValue();
        }

        switch(numeral.getType()) {
            case INT:
            case LONG:
                return BigDecimal.valueOf(numeral.getNumber().longValue());
            case BIG_INT:
                return new BigDecimal((BigInteger) numeral.getNumber(), Operations.getContext());
            case FLOAT:
            case DOUBLE:
                return BigDecimal.valueOf(numeral.getNumber().doubleValue());
            case BIG_DEC:
                return (BigDecimal) numeral.getNumber();
        }

        throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.unknownType(numeral.getType()),
                    IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return a {@link Numeral} backed with the smallest type that can hold the specified Numeral's value. This method
     * does not convert between integer and decimal types.
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static Numeral toSmallestType(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(numeral.getType()) {
            case INT:
            case FLOAT:
                return numeral;
            case LONG:
                if(!isOutOfIntRange(numeral.longValue())) {
                    return toIntNumeral(numeral);
                }
                return numeral;
            case BIG_INT:
                if(!isOutOfIntRange(numeral.bigIntValue())) {
                    return toIntNumeral(numeral);
                }
                if(!isOutOfLongRange(numeral.bigIntValue())) {
                    return toLongNumeral(numeral);
                }
                return numeral;
            case DOUBLE:
                if(!isOutOfFloatRange(numeral.doubleValue())) {
                    return toFloatNumeral(numeral);
                }
                return numeral;
            case BIG_DEC:
                if(!isOutOfFloatRange(numeral.bigDecValue())) {
                    return toFloatNumeral(numeral);
                }
                if(!isOutOfDoubleRange(numeral.bigDecValue())) {
                    return toDoubleNumeral(numeral);
                }
                return numeral;
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return a {@link Numeral} backed with the smallest integer type that can hold the specified Numeral's value,
     * unless the Numeral is already an integer, in which case the specified Numeral is returned
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static Numeral toInteger(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral.getType().isInteger()) {
            return numeral;
        }

        if(!isOutOfIntRange(numeral)) {
            return toIntNumeral(numeral);
        }

        if(!isOutOfLongRange(numeral)) {
            return toLongNumeral(numeral);
        }

        return toBigIntNumeral(numeral);
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return a {@link Numeral} backed with the smallest decimal type that can hold the specified Numeral's value,
     * unless the Numeral is already a decimal, in which case the specified Numeral is returned
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static Numeral toDecimal(Numeral numeral) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        if(numeral.getType().isDecimal()) {
            return numeral;
        }

        if(!isOutOfFloatRange(numeral)) {
            return toFloatNumeral(numeral);
        }

        if(!isOutOfDoubleRange(numeral)) {
            return toDoubleNumeral(numeral);
        }

        return toBigDecNumeral(numeral);
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @param type type for the {@link Numeral} to be converted to
     * @return a {@link Numeral} backed with the specified type
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is outside the specified type's
     * bounds
     * @throws NullPointerException if the specified type is {@code null} or if the {@link Numeral} returns {@code null}
     * for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the specified type is not {@code null} or if the {@link Numeral} does not
     * return {@code null} for {@link Numeral#getType()}, but the value is not recognised as any of the values of
     * {@link NumeralType}. This should never happen
     */
    public static Numeral toNumeral(Numeral numeral, NumeralType type) {
        if(numeral.getType() == null) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.NULL_NUMBER_TYPE,
                    NullPointerException::new);
        }

        switch(type) {
            case INT:
                return toIntNumeral(numeral);
            case LONG:
                return toLongNumeral(numeral);
            case BIG_INT:
                return toBigIntNumeral(numeral);
            case FLOAT:
                return toFloatNumeral(numeral);
            case DOUBLE:
                return toDoubleNumeral(numeral);
            case BIG_DEC:
                return toBigDecNumeral(numeral);
        }

        throw LogUtils.logExceptionAndGet(logger,
                MathStrings.unknownType(numeral.getType()),
                IllegalArgumentException::new);
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link Numeral} backed by an {@code int} with the specified Numeral's value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Integer#MIN_VALUE} or greater than {@link Integer#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static IntNumeral toIntNumeral(Numeral numeral) {
        if(numeral instanceof IntNumeral) {
            return (IntNumeral) numeral;
        }

        return Numerals.valueOf(toInt(numeral));
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link Numeral} backed by a {@code long} with the specified Numeral's value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Long#MIN_VALUE} or greater than {@link Long#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static LongNumeral toLongNumeral(Numeral numeral) {
        if(numeral instanceof LongNumeral) {
            return (LongNumeral) numeral;
        }

        return Numerals.valueOf(toLong(numeral));
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link Numeral} backed by a {@link BigInteger} with the specified Numeral's value
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static BigIntNumeral toBigIntNumeral(Numeral numeral) {
        if(numeral instanceof BigIntNumeral) {
            return (BigIntNumeral) numeral;
        }

        return Numerals.valueOf(toBigInt(numeral));
    }

    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link Numeral} backed by a {@code float} with the specified Numeral's value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Float#MAX_VALUE -Float.MAX_VALUE} or greater than {@link Float#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static FloatNumeral toFloatNumeral(Numeral numeral) {
        if(numeral instanceof FloatNumeral) {
            return (FloatNumeral) numeral;
        }

        return Numerals.valueOf(toFloat(numeral));
    }


    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link Numeral} backed by a {@code double} with the specified Numeral's value
     *
     * @throws ExtendedArithmeticException if the value of the specified {@link Numeral} is smaller than
     * {@link Double#MAX_VALUE -Double.MAX_VALUE} or greater than {@link Double#MAX_VALUE}
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static DoubleNumeral toDoubleNumeral(Numeral numeral) {
        if(numeral instanceof DoubleNumeral) {
            return (DoubleNumeral) numeral;
        }

        return Numerals.valueOf(toDouble(numeral));
    }


    /**
     * @param numeral {@link Numeral} to be converted
     * @return {@link Numeral} backed by a {@link BigDecimal} with the specified Numeral's value
     *
     * @throws NullPointerException if the {@link Numeral} returns {@code null} for {@link Numeral#getType()}
     * @throws IllegalArgumentException if the {@link Numeral} does not return {@code null} for
     * {@link Numeral#getType()}, but the value is not recognised as any of the values of {@link NumeralType}. This
     * should never happen
     */
    public static BigDecNumeral toBigDecNumeral(Numeral numeral) {
        if(numeral instanceof BigDecNumeral) {
            return (BigDecNumeral) numeral;
        }

        return Numerals.valueOf(toBigDec(numeral));
    }

    private static int toIntInRange(float f) {
        if(isOutOfIntRange(f)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.intOverflow(f),
                    ArithmeticException::new);
        }

        return (int) f;
    }

    private static int toIntInRange(double d) {
        if(isOutOfIntRange(d)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.intOverflow(d),
                    ArithmeticException::new);
        }

        return (int) d;
    }

    private static int toIntInRange(BigDecimal bd) {
        if(isOutOfIntRange(bd)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.intOverflow(bd),
                    ArithmeticException::new);
        }

        return bd.intValue();
    }

    private static long toLongInRange(float f) {
        if(isOutOfLongRange(f)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.longOverflow(f),
                    ArithmeticException::new);
        }

        return (long) f;
    }

    private static long toLongInRange(double d) {
        if(isOutOfLongRange(d)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.longOverflow(d),
                    ArithmeticException::new);
        }

        return (long) d;
    }

    private static long toLongInRange(BigDecimal bd) {
        if(isOutOfLongRange(bd)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.longOverflow(bd),
                    ArithmeticException::new);
        }

        return bd.longValue();
    }

    private static float toFloatInRange(BigInteger bi) {
        return toFloatInRange(new BigDecimal(bi));
    }

    private static float toFloatInRange(double d) {
        if(isOutOfFloatRange(d)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.floatOverflow(d),
                    ArithmeticException::new);
        }

        return (float) d;
    }

    private static float toFloatInRange(BigDecimal bd) {
        if(isOutOfFloatRange(bd)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.floatOverflow(bd),
                    ArithmeticException::new);
        }

        return bd.floatValue();
    }

    private static double toDoubleInRange(BigInteger bi) {
        return toDoubleInRange(new BigDecimal(bi));
    }

    private static double toDoubleInRange(BigDecimal bd) {
        if(isOutOfDoubleRange(bd)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.doubleOverflow(bd),
                    ArithmeticException::new);
        }

        return bd.doubleValue();
    }
}
