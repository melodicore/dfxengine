package me.datafox.dfxengine.math.utils;

import ch.obermuhlner.math.big.BigDecimalMath;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.numeral.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static me.datafox.dfxengine.math.api.NumeralType.BIG_DEC;
import static me.datafox.dfxengine.math.utils.RangeUtils.*;

/**
 * @author datafox
 */
public class OperationUtils {
    private static MathContext CONTEXT = MathContext.DECIMAL128;

    public static MathContext getContext() {
        return CONTEXT;
    }

    public static void setContext(MathContext context) {
        CONTEXT = context;
    }

    public static Numeral add(Numeral augend, Numeral addend) {
        if(isZero(augend)) {
            return addend;
        }
        if(isZero(addend)) {
            return augend;
        }
        switch(getSignificantType(augend, addend)) {
            case INT:
                return add(augend.intValue(), addend.intValue());
            case LONG:
                return add(augend.longValue(), addend.longValue());
            case BIG_INT:
                return add(augend.bigIntValue(), addend.bigIntValue());
            case FLOAT:
                return add(augend.floatValue(), addend.floatValue());
            case DOUBLE:
                return add(augend.doubleValue(), addend.doubleValue());
            case BIG_DEC:
                return add(augend.bigDecValue(), addend.bigDecValue());
        }
        throw new RuntimeException("this should never happen");
    }

    public static Numeral sub(Numeral minuend, Numeral subtrahend) {
        if(isZero(subtrahend)) {
            return minuend;
        }
        switch(getSignificantType(minuend, subtrahend)) {
            case INT:
                return sub(minuend.intValue(), subtrahend.intValue());
            case LONG:
                return sub(minuend.longValue(), subtrahend.longValue());
            case BIG_INT:
                return sub(minuend.bigIntValue(), subtrahend.bigIntValue());
            case FLOAT:
                return sub(minuend.floatValue(), subtrahend.floatValue());
            case DOUBLE:
                return sub(minuend.doubleValue(), subtrahend.doubleValue());
            case BIG_DEC:
                return sub(minuend.bigDecValue(), subtrahend.bigDecValue());
        }
        throw new RuntimeException("this should never happen");
    }

    public static Numeral subReversed(Numeral subtrahend, Numeral minuend) {
        return sub(minuend, subtrahend);
    }

    public static Numeral mul(Numeral multiplicand, Numeral multiplier) {
        if(isZero(multiplicand) || isZero(multiplier)) {
            return new IntNumeral(0);
        }
        if(isOne(multiplicand)) {
            return multiplier;
        }
        if(isOne(multiplier)) {
            return multiplicand;
        }
        switch(getSignificantType(multiplicand, multiplier)) {
            case INT:
                return mul(multiplicand.intValue(), multiplier.intValue());
            case LONG:
                return mul(multiplicand.longValue(), multiplier.longValue());
            case BIG_INT:
                return mul(multiplicand.bigIntValue(), multiplier.bigIntValue());
            case FLOAT:
                return mul(multiplicand.floatValue(), multiplier.floatValue());
            case DOUBLE:
                return mul(multiplicand.doubleValue(), multiplier.doubleValue());
            case BIG_DEC:
                return mul(multiplicand.bigDecValue(), multiplier.bigDecValue());
        }
        throw new RuntimeException("this should never happen");
    }

    public static Numeral div(Numeral dividend, Numeral divisor) {
        if(isZero(divisor)) {
            throw new ArithmeticException("divisor is zero");
        }
        if(isOne(divisor)) {
            return dividend;
        }
        NumeralType significantType = getSignificantType(dividend, divisor);
        switch(getSignificantType(dividend, divisor)) {
            case INT:
                return div(dividend.intValue(), divisor.intValue());
            case LONG:
                return div(dividend.longValue(), divisor.longValue());
            case BIG_INT:
                return div(dividend.bigIntValue(), divisor.bigIntValue());
            case FLOAT:
                return div(dividend.floatValue(), divisor.floatValue());
            case DOUBLE:
                return div(dividend.doubleValue(), divisor.doubleValue());
            case BIG_DEC:
                return div(dividend.bigDecValue(), divisor.bigDecValue());
        }
        throw new RuntimeException("this should never happen");
    }

    public static Numeral divReversed(Numeral divisor, Numeral dividend) {
        return div(dividend, divisor);
    }

    public static Numeral pow(Numeral base, Numeral exponent) {
        if(isZero(base)) {
            return new IntNumeral(0);
        }
        if(isOne(base) || isZero(exponent)) {
            return new IntNumeral(1);
        }
        if(isOne(exponent)) {
            return base;
        }
        switch(getSignificantType(base, exponent)) {
            case INT:
                return pow(base.intValue(), exponent.intValue());
            case LONG:
                return pow(base.longValue(), exponent.longValue());
            case BIG_INT:
                return pow(base.bigIntValue(), exponent.bigIntValue());
            case FLOAT:
                return pow(base.floatValue(), exponent.floatValue());
            case DOUBLE:
                return pow(base.doubleValue(), exponent.doubleValue());
            case BIG_DEC:
                return pow(base.bigDecValue(), exponent.bigDecValue());
        }
        throw new RuntimeException("this should never happen");
    }

    public static int compare(Numeral numeral, Numeral other) {
        switch(getSignificantType(numeral, other)) {
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
        throw new RuntimeException("this should never happen");
    }

    public static Numeral add(int augend, int addend) {
        int result = augend + addend;
        if(((augend ^ result) & (addend ^ result)) < 0) {
            return add((long) augend, addend);
        }
        return new IntNumeral(result);
    }

    public static Numeral add(long augend, long addend) {
        long result = augend + addend;
        if(((augend ^ result) & (addend ^ result)) < 0) {
            return add(BigInteger.valueOf(augend), BigInteger.valueOf(addend));
        }
        return new LongNumeral(result);
    }

    public static BigIntNumeral add(BigInteger augend, BigInteger addend) {
        return new BigIntNumeral(augend.add(addend));
    }

    public static Numeral add(float augend, float addend) {
        float result = augend + addend;
        if(isOutOfFloatRange(result)) {
            return add((double) augend, addend);
        }
        return new FloatNumeral(result);
    }

    public static Numeral add(double augend, double addend) {
        double result = augend + addend;
        if(isOutOfDoubleRange(result)) {
            return add(BigDecimal.valueOf(augend), BigDecimal.valueOf(addend));
        }
        return new DoubleNumeral(result);
    }

    public static BigDecNumeral add(BigDecimal augend, BigDecimal addend) {
        return new BigDecNumeral(augend.add(addend, CONTEXT));
    }

    public static Numeral sub(int minuend, int subtrahend) {
        int result = minuend - subtrahend;
        if(((minuend ^ subtrahend) & (minuend ^ result)) < 0) {
            return sub((long) minuend, subtrahend);
        }
        return new IntNumeral(result);
    }

    public static Numeral sub(long minuend, long subtrahend) {
        long result = minuend - subtrahend;
        if(((minuend ^ subtrahend) & (minuend ^ result)) < 0) {
            return sub(BigInteger.valueOf(minuend), BigInteger.valueOf(subtrahend));
        }
        return new LongNumeral(result);
    }

    public static BigIntNumeral sub(BigInteger minuend, BigInteger subtrahend) {
        return new BigIntNumeral(minuend.subtract(subtrahend));
    }

    public static Numeral sub(float minuend, float subtrahend) {
        float result = minuend - subtrahend;
        if(isOutOfFloatRange(result)) {
            return sub((double) minuend, subtrahend);
        }
        return new FloatNumeral(result);
    }

    public static Numeral sub(double minuend, double subtrahend) {
        double result = minuend - subtrahend;
        if(isOutOfDoubleRange(result)) {
            return sub(BigDecimal.valueOf(minuend), BigDecimal.valueOf(subtrahend));
        }
        return new DoubleNumeral(result);
    }

    public static BigDecNumeral sub(BigDecimal minuend, BigDecimal subtrahend) {
        return new BigDecNumeral(minuend.subtract(subtrahend, CONTEXT));
    }

    public static Numeral mul(int multiplicand, int multiplier) {
        long result = (long) multiplicand * multiplier;
        if((int) result != result) {
            return mul((long) multiplicand, multiplier);
        }
        return new IntNumeral((int) result);
    }

    public static Numeral mul(long multiplicand, long multiplier) {
        long result = multiplicand * multiplier;
        long absMultiplicand = Math.abs(multiplicand);
        long absMultiplier = Math.abs(multiplier);
        if((absMultiplicand | absMultiplier) >>> 31 != 0) {
            if (((multiplier != 0) && (result / multiplier != multiplicand)) ||
                    (multiplicand == Long.MIN_VALUE && multiplier == -1)) {
                return mul(BigInteger.valueOf(multiplicand), BigInteger.valueOf(multiplier));
            }
        }
        return new LongNumeral(result);
    }

    public static BigIntNumeral mul(BigInteger multiplicand, BigInteger multiplier) {
        return new BigIntNumeral(multiplicand.multiply(multiplier));
    }

    public static Numeral mul(float multiplicand, float multiplier) {
        float result = multiplicand * multiplier;
        if(isOutOfFloatRange(result)) {
            return mul((double) multiplicand, multiplier);
        }
        return new FloatNumeral(result);
    }

    public static Numeral mul(double multiplicand, double multiplier) {
        double result = multiplicand * multiplier;
        if(isOutOfDoubleRange(result)) {
            return mul(BigDecimal.valueOf(multiplicand), BigDecimal.valueOf(multiplier));
        }
        return new DoubleNumeral(result);
    }

    public static BigDecNumeral mul(BigDecimal multiplicand, BigDecimal multiplier) {
        return new BigDecNumeral(multiplicand.multiply(multiplier, CONTEXT));
    }

    public static Numeral div(int dividend, int divisor) {
        return new IntNumeral(dividend / divisor);
    }

    public static Numeral div(long dividend, long divisor) {
        return new LongNumeral(dividend / divisor);
    }

    public static BigIntNumeral div(BigInteger dividend, BigInteger divisor) {
        return new BigIntNumeral(dividend.divide(divisor));
    }

    public static Numeral div(float dividend, float divisor) {
        float result = dividend / divisor;
        if(isOutOfFloatRange(result)) {
            return div((double) dividend, divisor);
        }
        return new FloatNumeral(result);
    }

    public static Numeral div(double dividend, double divisor) {
        double result = dividend / divisor;
        if(isOutOfDoubleRange(result)) {
            return div(BigDecimal.valueOf(dividend), BigDecimal.valueOf(divisor));
        }
        return new DoubleNumeral(result);
    }

    public static BigDecNumeral div(BigDecimal dividend, BigDecimal divisor) {
        return new BigDecNumeral(dividend.divide(divisor, CONTEXT));
    }

    public static Numeral pow(int base, int exponent) {
        double value = Math.pow(base, exponent);
        if(isOutOfDoubleRange(value)) {
            return pow(BigInteger.valueOf(base), BigInteger.valueOf(exponent));
        }
        if(isOutOfIntRange(value)) {
            return new LongNumeral((long) value);
        }
        return new IntNumeral((int) value);
    }

    public static Numeral pow(long base, long exponent) {
        double value = Math.pow(base, exponent);
        if(isOutOfLongRange(value)) {
            return pow(BigInteger.valueOf(base), BigInteger.valueOf(exponent));
        }
        return new LongNumeral((long) value);
    }

    public static Numeral pow(BigInteger base, BigInteger exponent) {
        try {
            return new BigIntNumeral(base.pow(exponent.intValueExact()));
        } catch(ArithmeticException ignored) {
            return pow(new BigDecimal(base), new BigDecimal(exponent));
        }
    }

    public static Numeral pow(float base, float exponent) {
        double value = Math.pow(base, exponent);
        if(isOutOfDoubleRange(value)) {
            return pow(BigDecimal.valueOf(base), BigDecimal.valueOf(exponent));
        }
        if(isOutOfFloatRange((float) value)) {
            return new DoubleNumeral(value);
        }
        return new FloatNumeral((float) value);
    }

    public static Numeral pow(double base, double exponent) {
        double value = Math.pow(base, exponent);
        if(isOutOfDoubleRange(value)) {
            return pow(BigDecimal.valueOf(base), BigDecimal.valueOf(exponent));
        }
        return new DoubleNumeral(value);
    }

    public static Numeral pow(BigDecimal base, BigDecimal exponent) {
        return new BigDecNumeral(BigDecimalMath.pow(base, exponent, CONTEXT));
    }

    public static NumeralType getSignificantType(Numeral numeral1, Numeral numeral2) {
        if(numeral1.getType().equals(numeral2.getType())) {
            return numeral1.getType();
        }

        return getSignificantType(new Numeral[] { numeral1, numeral2 });
    }

    public static NumeralType getSignificantType(Numeral ... numerals) {
        if(numerals.length == 0) {
            throw new IllegalArgumentException("empty array");
        }

        NumeralType significantType = numerals[0].getType();

        boolean first = true;

        for(Numeral numeral : numerals) {
            if(first) {
                first = false;
                continue;
            }

            switch(numeral.getType()) {
                case LONG:
                    if(significantType.equals(NumeralType.INT)) {
                        significantType = NumeralType.LONG;
                    }
                    break;
                case BIG_INT:
                    switch(significantType) {
                        case INT:
                        case LONG:
                            significantType = NumeralType.BIG_INT;
                            break;
                        case FLOAT:
                        case DOUBLE:
                            return BIG_DEC;
                    }
                    break;
                case FLOAT:
                    switch(significantType) {
                        case INT:
                        case LONG:
                            significantType = NumeralType.FLOAT;
                            break;
                        case BIG_INT:
                            return BIG_DEC;
                    }
                    break;
                case DOUBLE:
                    switch(significantType) {
                        case INT:
                        case LONG:
                        case FLOAT:
                            significantType = NumeralType.DOUBLE;
                            break;
                        case BIG_INT:
                            return BIG_DEC;
                    }
                    break;
                case BIG_DEC:
                    return BIG_DEC;
            }
        }
        return significantType;
    }
}
