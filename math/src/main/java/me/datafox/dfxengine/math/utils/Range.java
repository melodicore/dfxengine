package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author datafox
 */
public class Range {
    public static final BigInteger INT_MIN_INT = BigInteger.valueOf(Integer.MIN_VALUE);
    public static final BigInteger INT_MAX_INT = BigInteger.valueOf(Integer.MAX_VALUE);
    public static final BigInteger INT_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    public static final BigInteger INT_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    public static final BigDecimal DEC_MIN_INT = new BigDecimal(INT_MIN_INT);
    public static final BigDecimal DEC_MAX_INT = new BigDecimal(INT_MAX_INT);
    public static final BigDecimal DEC_MIN_LONG = new BigDecimal(INT_MIN_LONG);
    public static final BigDecimal DEC_MAX_LONG = new BigDecimal(INT_MAX_LONG);

    public static final BigDecimal DEC_MIN_FLOAT = BigDecimal.valueOf(-Float.MAX_VALUE);
    public static final BigDecimal DEC_MAX_FLOAT = BigDecimal.valueOf(Float.MAX_VALUE);
    public static final BigDecimal DEC_MIN_DOUBLE = BigDecimal.valueOf(-Double.MAX_VALUE);
    public static final BigDecimal DEC_MAX_DOUBLE = BigDecimal.valueOf(Double.MAX_VALUE);

    public static final BigInteger INT_MIN_FLOAT = DEC_MIN_FLOAT.toBigInteger();
    public static final BigInteger INT_MAX_FLOAT = DEC_MAX_FLOAT.toBigInteger();
    public static final BigInteger INT_MIN_DOUBLE = DEC_MIN_DOUBLE.toBigInteger();
    public static final BigInteger INT_MAX_DOUBLE = DEC_MAX_DOUBLE.toBigInteger();

    public static boolean isOutOfRange(Numeral numeral, NumeralType type) {
        switch(type) {
            case INT:
                return isOutOfIntRange(numeral);
            case LONG:
                return isOutOfLongRange(numeral);
            case BIG_INT:
            case BIG_DEC:
                return false;
            case FLOAT:
                return isOutOfFloatRange(numeral);
            case DOUBLE:
                return isOutOfDoubleRange(numeral);
        }

        throw new IllegalArgumentException("unknown type");
    }

    public static boolean isOutOfIntRange(Numeral numeral) {
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

        throw new IllegalArgumentException("unknown type");
    }

    public static boolean isOutOfIntRange(long l) {
        return l > Integer.MAX_VALUE || l < Integer.MIN_VALUE;
    }

    public static boolean isOutOfIntRange(BigInteger bi) {
        return bi.compareTo(INT_MAX_INT) > 0 || bi.compareTo(INT_MIN_INT) < 0;
    }

    public static boolean isOutOfIntRange(float f) {
        return f > Integer.MAX_VALUE || f < Integer.MIN_VALUE;
    }

    public static boolean isOutOfIntRange(double d) {
        return d > Integer.MAX_VALUE || d < Integer.MIN_VALUE;
    }

    public static boolean isOutOfIntRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_INT) > 0 || bd.compareTo(DEC_MIN_INT) < 0;
    }

    public static boolean isOutOfLongRange(Numeral numeral) {
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

        throw new IllegalArgumentException("unknown type");
    }

    public static boolean isOutOfLongRange(BigInteger bi) {
        return bi.compareTo(INT_MAX_LONG) > 0 || bi.compareTo(INT_MIN_LONG) < 0;
    }

    public static boolean isOutOfLongRange(float f) {
        return f > Long.MAX_VALUE || f < Long.MIN_VALUE;
    }

    public static boolean isOutOfLongRange(double d) {
        return d > Long.MAX_VALUE || d < Long.MIN_VALUE;
    }

    public static boolean isOutOfLongRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_LONG) > 0 || bd.compareTo(DEC_MIN_LONG) < 0;
    }

    public static boolean isOutOfFloatRange(Numeral numeral) {
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

        throw new IllegalArgumentException("unknown type");
    }

    public static boolean isOutOfFloatRange(BigInteger bi) {
        return bi.compareTo(INT_MAX_FLOAT) > 0 || bi.compareTo(INT_MIN_FLOAT) < 0;
    }

    public static boolean isOutOfFloatRange(float f) {
        return f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY;
    }

    public static boolean isOutOfFloatRange(double d) {
        return d > Float.MAX_VALUE || d < -Float.MAX_VALUE;
    }

    public static boolean isOutOfFloatRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_FLOAT) > 0 || bd.compareTo(DEC_MIN_FLOAT) < 0;
    }

    public static boolean isOutOfDoubleRange(Numeral numeral) {
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

        throw new IllegalArgumentException("unknown type");
    }

    public static boolean isOutOfDoubleRange(BigInteger bi) {
        return bi.compareTo(INT_MAX_DOUBLE) > 0 || bi.compareTo(INT_MIN_DOUBLE) < 0;
    }

    public static boolean isOutOfDoubleRange(double d) {
        return d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY;
    }

    public static boolean isOutOfDoubleRange(BigDecimal bd) {
        return bd.compareTo(DEC_MAX_DOUBLE) > 0 || bd.compareTo(DEC_MIN_DOUBLE) < 0;
    }
}
