package me.datafox.dfxengine.math.utils;

import ch.obermuhlner.math.big.BigDecimalMath;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.numeral.BigDecNumeral;
import me.datafox.dfxengine.math.numeral.BigIntNumeral;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static me.datafox.dfxengine.math.utils.Numerals.*;
import static me.datafox.dfxengine.math.utils.Range.*;

/**
 * @author datafox
 */
public class Operations {
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
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral subtract(Numeral minuend, Numeral subtrahend) {
        if(isZero(subtrahend)) {
            return minuend;
        }
        if(compare(minuend, subtrahend) == 0) {
            return valueOf(0);
        }
        switch(getSignificantType(minuend, subtrahend)) {
            case INT:
                return subtract(minuend.intValue(), subtrahend.intValue());
            case LONG:
                return subtract(minuend.longValue(), subtrahend.longValue());
            case BIG_INT:
                return subtract(minuend.bigIntValue(), subtrahend.bigIntValue());
            case FLOAT:
                return subtract(minuend.floatValue(), subtrahend.floatValue());
            case DOUBLE:
                return subtract(minuend.doubleValue(), subtrahend.doubleValue());
            case BIG_DEC:
                return subtract(minuend.bigDecValue(), subtrahend.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral multiply(Numeral multiplicand, Numeral multiplier) {
        if(isZero(multiplicand) || isZero(multiplier)) {
            return valueOf(0);
        }
        if(isOne(multiplicand)) {
            return multiplier;
        }
        if(isOne(multiplier)) {
            return multiplicand;
        }
        switch(getSignificantType(multiplicand, multiplier)) {
            case INT:
                return multiply(multiplicand.intValue(), multiplier.intValue());
            case LONG:
                return multiply(multiplicand.longValue(), multiplier.longValue());
            case BIG_INT:
                return multiply(multiplicand.bigIntValue(), multiplier.bigIntValue());
            case FLOAT:
                return multiply(multiplicand.floatValue(), multiplier.floatValue());
            case DOUBLE:
                return multiply(multiplicand.doubleValue(), multiplier.doubleValue());
            case BIG_DEC:
                return multiply(multiplicand.bigDecValue(), multiplier.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral divide(Numeral dividend, Numeral divisor) {
        if(isZero(divisor)) {
            throw new ArithmeticException("divisor is zero");
        }
        if(isZero(dividend)) {
            return Numerals.valueOf(0);
        }
        if(isOne(divisor)) {
            return dividend;
        }
        if(compare(dividend, divisor) == 0) {
            return valueOf(1);
        }
        NumeralType significantType = getSignificantType(dividend, divisor);
        switch(getSignificantType(dividend, divisor)) {
            case INT:
                return divide(dividend.intValue(), divisor.intValue());
            case LONG:
                return divide(dividend.longValue(), divisor.longValue());
            case BIG_INT:
                return divide(dividend.bigIntValue(), divisor.bigIntValue());
            case FLOAT:
                return divide(dividend.floatValue(), divisor.floatValue());
            case DOUBLE:
                return divide(dividend.doubleValue(), divisor.doubleValue());
            case BIG_DEC:
                return divide(dividend.bigDecValue(), divisor.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral inverse(Numeral numeral) {
        return divide(valueOf(1), numeral);
    }

    public static Numeral power(Numeral base, Numeral exponent) {
        if(isOne(base) || isZero(exponent)) {
            return valueOf(1);
        }
        if(isZero(base)) {
            return valueOf(0);
        }
        if(isOne(exponent)) {
            return base;
        }
        switch(getSignificantType(base, exponent)) {
            case INT:
                return power(base.intValue(), exponent.intValue());
            case LONG:
                return power(base.longValue(), exponent.longValue());
            case BIG_INT:
                return power(base.bigIntValue(), exponent.bigIntValue());
            case FLOAT:
                return power(base.floatValue(), exponent.floatValue());
            case DOUBLE:
                return power(base.doubleValue(), exponent.doubleValue());
            case BIG_DEC:
                return power(base.bigDecValue(), exponent.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral exp(Numeral numeral) {
        if(isZero(numeral)) {
            return valueOf(1);
        }
        switch(numeral.getType()) {
            case INT:
                return exp(numeral.intValue());
            case LONG:
                return exp(numeral.longValue());
            case BIG_INT:
                return exp(numeral.bigIntValue());
            case FLOAT:
                return exp(numeral.floatValue());
            case DOUBLE:
                return exp(numeral.doubleValue());
            case BIG_DEC:
                return exp(numeral.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral sqrt(Numeral numeral) {
        if(isZero(numeral)) {
            return valueOf(0);
        }
        if(isOne(numeral)) {
            return valueOf(1);
        }
        if(compare(numeral, valueOf(0)) < 0) {
            throw new ArithmeticException("root of negative number");
        }
        switch(numeral.getType()) {
            case INT:
                return sqrt(numeral.intValue());
            case LONG:
                return sqrt(numeral.longValue());
            case BIG_INT:
                return sqrt(numeral.bigIntValue());
            case FLOAT:
                return sqrt(numeral.floatValue());
            case DOUBLE:
                return sqrt(numeral.doubleValue());
            case BIG_DEC:
                return sqrt(numeral.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral cbrt(Numeral numeral) {
        if(isZero(numeral)) {
            return valueOf(0);
        }
        if(isOne(numeral)) {
            return valueOf(1);
        }
        if(compare(numeral, valueOf(0)) < 0) {
            throw new ArithmeticException("root of negative number");
        }
        switch(numeral.getType()) {
            case INT:
                return cbrt(numeral.intValue());
            case LONG:
                return cbrt(numeral.longValue());
            case BIG_INT:
                return cbrt(numeral.bigIntValue());
            case FLOAT:
                return cbrt(numeral.floatValue());
            case DOUBLE:
                return cbrt(numeral.doubleValue());
            case BIG_DEC:
                return cbrt(numeral.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral root(Numeral numeral, Numeral root) {
        if(isZero(root)) {
            throw new ArithmeticException("zeroth root");
        }
        if(isZero(numeral)) {
            return valueOf(0);
        }
        if(isOne(numeral)) {
            return valueOf(1);
        }
        if(isOne(root)) {
            return numeral;
        }
        if(compare(numeral, valueOf(0)) < 0) {
            throw new ArithmeticException("root of negative number");
        }
        switch(getSignificantType(numeral, root)) {
            case INT:
                return root(numeral.intValue(), root.intValue());
            case LONG:
                return root(numeral.longValue(), root.longValue());
            case BIG_INT:
                return root(numeral.bigIntValue(), root.bigIntValue());
            case FLOAT:
                return root(numeral.floatValue(), root.floatValue());
            case DOUBLE:
                return root(numeral.doubleValue(), root.doubleValue());
            case BIG_DEC:
                return root(numeral.bigDecValue(), root.bigDecValue());
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral log(Numeral numeral) {
        if(isZero(numeral) || compare(numeral, Numerals.valueOf(0)) < 0) {
            throw new ArithmeticException("logarithm of 0 or smaller");
        }
        if(isOne(numeral)) {
            return valueOf(0);
        }
        switch(numeral.getType()) {
            case INT:
                return(log(numeral.intValue()));
            case LONG:
                return(log(numeral.longValue()));
            case BIG_INT:
                return(log(numeral.bigIntValue()));
            case FLOAT:
                return(log(numeral.floatValue()));
            case DOUBLE:
                return(log(numeral.doubleValue()));
            case BIG_DEC:
                return(log(numeral.bigDecValue()));
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral log2(Numeral numeral) {
        if(isZero(numeral) || compare(numeral, Numerals.valueOf(0)) < 0) {
            throw new ArithmeticException("logarithm of 0 or smaller");
        }
        if(isOne(numeral)) {
            return valueOf(0);
        }
        switch(numeral.getType()) {
            case INT:
                return(log2(numeral.intValue()));
            case LONG:
                return(log2(numeral.longValue()));
            case BIG_INT:
                return(log2(numeral.bigIntValue()));
            case FLOAT:
                return(log2(numeral.floatValue()));
            case DOUBLE:
                return(log2(numeral.doubleValue()));
            case BIG_DEC:
                return(log2(numeral.bigDecValue()));
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral log10(Numeral numeral) {
        if(isZero(numeral) || compare(numeral, Numerals.valueOf(0)) < 0) {
            throw new ArithmeticException("logarithm of 0 or smaller");
        }
        if(isOne(numeral)) {
            return valueOf(0);
        }
        switch(numeral.getType()) {
            case INT:
                return(log10(numeral.intValue()));
            case LONG:
                return(log10(numeral.longValue()));
            case BIG_INT:
                return(log10(numeral.bigIntValue()));
            case FLOAT:
                return(log10(numeral.floatValue()));
            case DOUBLE:
                return(log10(numeral.doubleValue()));
            case BIG_DEC:
                return(log10(numeral.bigDecValue()));
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral logN(Numeral numeral, Numeral base) {
        if(isZero(numeral) || compare(numeral, Numerals.valueOf(0)) < 0) {
            throw new ArithmeticException("logarithm of 0 or smaller");
        }
        if(isZero(base) || compare(base, Numerals.valueOf(0)) < 0) {
            throw new ArithmeticException("logarithm base of 0 or smaller");
        }
        if(isOne(base)) {
            throw new ArithmeticException("logarithm base of 1");
        }
        if(isOne(numeral)) {
            return valueOf(0);
        }
        switch(numeral.getType()) {
            case INT:
                return(logN(numeral.intValue(), base.intValue()));
            case LONG:
                return(logN(numeral.longValue(), base.longValue()));
            case BIG_INT:
                return(logN(numeral.bigIntValue(), base.bigIntValue()));
            case FLOAT:
                return(logN(numeral.floatValue(), base.floatValue()));
            case DOUBLE:
                return(logN(numeral.doubleValue(), base.doubleValue()));
            case BIG_DEC:
                return(logN(numeral.bigDecValue(), base.bigDecValue()));
        }
        throw new IllegalArgumentException("unknown type");
    }

    

    public static Numeral add(int augend, int addend) {
        int result = augend + addend;
        if(((augend ^ result) & (addend ^ result)) < 0) {
            return add((long) augend, addend);
        }
        return valueOf(result);
    }

    public static Numeral add(long augend, long addend) {
        long result = augend + addend;
        if(((augend ^ result) & (addend ^ result)) < 0) {
            return add(BigInteger.valueOf(augend), BigInteger.valueOf(addend));
        }
        return valueOf(result);
    }

    public static BigIntNumeral add(BigInteger augend, BigInteger addend) {
        return valueOf(augend.add(addend));
    }

    public static Numeral add(float augend, float addend) {
        float result = augend + addend;
        if(!Float.isFinite(result)) {
            return add((double) augend, addend);
        }
        return valueOf(result);
    }

    public static Numeral add(double augend, double addend) {
        double result = augend + addend;
        if(!Double.isFinite(result)) {
            return add(BigDecimal.valueOf(augend), BigDecimal.valueOf(addend));
        }
        return valueOf(result);
    }

    public static BigDecNumeral add(BigDecimal augend, BigDecimal addend) {
        return valueOf(augend.add(addend, CONTEXT));
    }

    public static Numeral subtract(int minuend, int subtrahend) {
        int result = minuend - subtrahend;
        if(((minuend ^ subtrahend) & (minuend ^ result)) < 0) {
            return subtract((long) minuend, subtrahend);
        }
        return valueOf(result);
    }

    public static Numeral subtract(long minuend, long subtrahend) {
        long result = minuend - subtrahend;
        if(((minuend ^ subtrahend) & (minuend ^ result)) < 0) {
            return subtract(BigInteger.valueOf(minuend), BigInteger.valueOf(subtrahend));
        }
        return valueOf(result);
    }

    public static BigIntNumeral subtract(BigInteger minuend, BigInteger subtrahend) {
        return valueOf(minuend.subtract(subtrahend));
    }

    public static Numeral subtract(float minuend, float subtrahend) {
        float result = minuend - subtrahend;
        if(!Float.isFinite(result)) {
            return subtract((double) minuend, subtrahend);
        }
        return valueOf(result);
    }

    public static Numeral subtract(double minuend, double subtrahend) {
        double result = minuend - subtrahend;
        if(!Double.isFinite(result)) {
            return subtract(BigDecimal.valueOf(minuend), BigDecimal.valueOf(subtrahend));
        }
        return valueOf(result);
    }

    public static BigDecNumeral subtract(BigDecimal minuend, BigDecimal subtrahend) {
        return valueOf(minuend.subtract(subtrahend, CONTEXT));
    }

    public static Numeral multiply(int multiplicand, int multiplier) {
        long result = (long) multiplicand * multiplier;
        if((int) result != result) {
            return multiply((long) multiplicand, multiplier);
        }
        return valueOf((int) result);
    }

    public static Numeral multiply(long multiplicand, long multiplier) {
        long result = multiplicand * multiplier;
        long absMultiplicand = Math.abs(multiplicand);
        long absMultiplier = Math.abs(multiplier);
        if((absMultiplicand | absMultiplier) >>> 31 != 0) {
            if (((multiplier != 0) && (result / multiplier != multiplicand)) ||
                    (multiplicand == Long.MIN_VALUE && multiplier == -1)) {
                return multiply(BigInteger.valueOf(multiplicand), BigInteger.valueOf(multiplier));
            }
        }
        return valueOf(result);
    }

    public static BigIntNumeral multiply(BigInteger multiplicand, BigInteger multiplier) {
        return valueOf(multiplicand.multiply(multiplier));
    }

    public static Numeral multiply(float multiplicand, float multiplier) {
        float result = multiplicand * multiplier;
        if(!Float.isFinite(result)) {
            return multiply((double) multiplicand, multiplier);
        }
        return valueOf(result);
    }

    public static Numeral multiply(double multiplicand, double multiplier) {
        double result = multiplicand * multiplier;
        if(!Double.isFinite(result)) {
            return multiply(BigDecimal.valueOf(multiplicand), BigDecimal.valueOf(multiplier));
        }
        return valueOf(result);
    }

    public static BigDecNumeral multiply(BigDecimal multiplicand, BigDecimal multiplier) {
        return valueOf(multiplicand.multiply(multiplier, CONTEXT));
    }

    public static Numeral divide(int dividend, int divisor) {
        return valueOf(dividend / divisor);
    }

    public static Numeral divide(long dividend, long divisor) {
        return valueOf(dividend / divisor);
    }

    public static BigIntNumeral divide(BigInteger dividend, BigInteger divisor) {
        return valueOf(dividend.divide(divisor));
    }

    public static Numeral divide(float dividend, float divisor) {
        float result = dividend / divisor;
        if(!Float.isFinite(result)) {
            return divide((double) dividend, divisor);
        }
        return valueOf(result);
    }

    public static Numeral divide(double dividend, double divisor) {
        double result = dividend / divisor;
        if(!Double.isFinite(result)) {
            return divide(BigDecimal.valueOf(dividend), BigDecimal.valueOf(divisor));
        }
        return valueOf(result);
    }

    public static BigDecNumeral divide(BigDecimal dividend, BigDecimal divisor) {
        return valueOf(dividend.divide(divisor, CONTEXT));
    }

    public static Numeral power(int base, int exponent) {
        double value = Math.pow(base, exponent);
        if(isOutOfLongRange(value)) {
            return power(BigInteger.valueOf(base), BigInteger.valueOf(exponent));
        }
        if(isOutOfIntRange(value)) {
            return valueOf((long) value);
        }
        return valueOf((int) value);
    }

    public static Numeral power(long base, long exponent) {
        double value = Math.pow(base, exponent);
        if(isOutOfLongRange(value)) {
            return power(BigInteger.valueOf(base), BigInteger.valueOf(exponent));
        }
        return valueOf((long) value);
    }

    public static Numeral power(BigInteger base, BigInteger exponent) {
        if(isOutOfIntRange(exponent)) {
            return valueOf(BigDecimalMath.pow(new BigDecimal(base), new BigDecimal(exponent), CONTEXT).toBigInteger());
        }
        return valueOf(base.pow(exponent.intValue()));
    }

    public static Numeral power(float base, float exponent) {
        double value = Math.pow(base, exponent);
        if(!Double.isFinite(value)) {
            return power(BigDecimal.valueOf(base), BigDecimal.valueOf(exponent));
        }
        if(isOutOfFloatRange(value)) {
            return valueOf(value);
        }
        return valueOf((float) value);
    }

    public static Numeral power(double base, double exponent) {
        double value = Math.pow(base, exponent);
        if(!Double.isFinite(value)) {
            return power(BigDecimal.valueOf(base), BigDecimal.valueOf(exponent));
        }
        return valueOf(value);
    }

    public static Numeral power(BigDecimal base, BigDecimal exponent) {
        return valueOf(BigDecimalMath.pow(base, exponent, CONTEXT));
    }

    public static Numeral exp(int value) {
        double result = Math.exp(value);
        if(isOutOfLongRange(result)) {
            return exp(BigInteger.valueOf(value));
        }
        if(isOutOfIntRange(result)) {
            return valueOf((long) result);
        }
        return valueOf((int) result);
    }

    public static Numeral exp(long value) {
        double result = Math.exp(value);
        if(isOutOfLongRange(result)) {
            return exp(BigInteger.valueOf(value));
        }
        return valueOf((long) result);
    }

    public static Numeral exp(BigInteger value) {
        return valueOf(BigDecimalMath.exp(new BigDecimal(value), CONTEXT).toBigInteger());
    }

    public static Numeral exp(float value) {
        double result = Math.exp(value);
        if(!Double.isFinite(result)) {
            return exp(BigDecimal.valueOf(value));
        }
        if(isOutOfFloatRange(result)) {
            return valueOf(result);
        }
        return valueOf((float) result);
    }

    public static Numeral exp(double value) {
        double result = Math.exp(value);
        if(!Double.isFinite(result)) {
            return exp(BigDecimal.valueOf(value));
        }
        return valueOf(result);
    }

    public static Numeral exp(BigDecimal value) {
        return valueOf(BigDecimalMath.exp(value, CONTEXT));
    }

    public static Numeral sqrt(int value) {
        return valueOf((int) Math.sqrt(value));
    }

    public static Numeral sqrt(long value) {
        return valueOf((long) Math.sqrt(value));
    }

    public static Numeral sqrt(BigInteger value) {
        return valueOf(value.sqrt());
    }

    public static Numeral sqrt(float value) {
        return valueOf((float) Math.sqrt(value));
    }

    public static Numeral sqrt(double value) {
        return valueOf(Math.sqrt(value));
    }

    public static Numeral sqrt(BigDecimal value) {
        return valueOf(value.sqrt(CONTEXT));
    }

    public static Numeral cbrt(int value) {
        return valueOf((int) Math.cbrt(value));
    }

    public static Numeral cbrt(long value) {
        return valueOf((long) Math.cbrt(value));
    }

    public static Numeral cbrt(BigInteger value) {
        return root(value, BigInteger.valueOf(3));
    }

    public static Numeral cbrt(float value) {
        return valueOf((float) Math.cbrt(value));
    }

    public static Numeral cbrt(double value) {
        return valueOf(Math.cbrt(value));
    }

    public static Numeral cbrt(BigDecimal value) {
        return root(value, BigDecimal.valueOf(3));
    }

    public static Numeral root(int value, int root) {
        return valueOf((int) Math.pow(value, 1d/root));
    }

    public static Numeral root(long value, long root) {
        return valueOf((long) Math.pow(value, 1d/root));
    }

    public static Numeral root(BigInteger value, BigInteger exponent) {
        return valueOf(BigDecimalMath.root(new BigDecimal(value), new BigDecimal(exponent), CONTEXT).toBigInteger());
    }

    public static Numeral root(float value, float root) {
        double result = Math.pow(value, 1d/root);
        if(!Double.isFinite(result)) {
            return root(BigDecimal.valueOf(value), BigDecimal.valueOf(root));
        }
        if(isOutOfFloatRange(result)) {
            return valueOf(result);
        }
        return valueOf((float) result);
    }

    public static Numeral root(double value, double root) {
        double result = Math.pow(value, 1d/root);
        if(!Double.isFinite(result)) {
            return root(BigDecimal.valueOf(value), BigDecimal.valueOf(root));
        }
        return valueOf(result);
    }

    public static Numeral root(BigDecimal value, BigDecimal root) {
        return valueOf(BigDecimalMath.root(value, root, CONTEXT));
    }

    public static Numeral log(int value) {
        return valueOf((int) Math.log(value));
    }

    public static Numeral log(long value) {
        return valueOf((long) Math.log(value));
    }

    public static Numeral log(BigInteger value) {
        return valueOf(BigDecimalMath.log(new BigDecimal(value), CONTEXT).toBigInteger());
    }

    public static Numeral log(float value) {
        return valueOf((float) Math.log(value));
    }

    public static Numeral log(double value) {
        return valueOf(Math.log(value));
    }

    public static Numeral log(BigDecimal value) {
        return valueOf(BigDecimalMath.log(value, CONTEXT));
    }

    public static Numeral log2(int value) {
        return valueOf((int) (Math.log(value) / Math.log(2)));
    }

    public static Numeral log2(long value) {
        return valueOf((long) (Math.log(value) / Math.log(2)));
    }

    public static Numeral log2(BigInteger value) {
        return valueOf(BigDecimalMath.log2(new BigDecimal(value), CONTEXT).toBigInteger());
    }
    
    public static Numeral log2(float value) {
        return valueOf((float) (Math.log(value) / Math.log(2)));
    }
    
    public static Numeral log2(double value) {
        return valueOf(Math.log(value) / Math.log(2));
    }
    
    public static Numeral log2(BigDecimal value) {
        return valueOf(BigDecimalMath.log2(value, CONTEXT));
    }

    public static Numeral log10(int value) {
        return valueOf((int) Math.log10(value));
    }

    public static Numeral log10(long value) {
        return valueOf((long) Math.log10(value));
    }

    public static Numeral log10(BigInteger value) {
        return valueOf(BigDecimalMath.log10(new BigDecimal(value), CONTEXT).toBigInteger());
    }

    public static Numeral log10(float value) {
        return valueOf((float) Math.log10(value));
    }

    public static Numeral log10(double value) {
        return valueOf(Math.log10(value));
    }

    public static Numeral log10(BigDecimal value) {
        return valueOf(BigDecimalMath.log10(value, CONTEXT));
    }

    public static Numeral logN(int value, int base) {
        return valueOf((int) (Math.log(value) / Math.log(base)));
    }

    public static Numeral logN(long value, long base) {
        return valueOf((long) (Math.log(value) / Math.log(base)));
    }

    public static Numeral logN(BigInteger value, BigInteger base) {
        return valueOf(BigDecimalMath.log(new BigDecimal(value), CONTEXT).divide(
                BigDecimalMath.log(new BigDecimal(base), CONTEXT), CONTEXT)
                .toBigInteger());
    }

    public static Numeral logN(float value, float base) {
        double result = (Math.log(value) / Math.log(base));
        if(!Double.isFinite(result)) {
            return logN(BigDecimal.valueOf(value), BigDecimal.valueOf(base));
        }
        if(isOutOfFloatRange(result)) {
            return valueOf(result);
        }
        return valueOf((float) result);
    }

    public static Numeral logN(double value, double base) {
        double result = (Math.log(value) / Math.log(base));
        if(!Double.isFinite(result)) {
            return logN(BigDecimal.valueOf(value), BigDecimal.valueOf(base));
        }
        return valueOf(result);
    }

    public static Numeral logN(BigDecimal value, BigDecimal base) {
        return valueOf(BigDecimalMath.log(value, CONTEXT).divide(
                BigDecimalMath.log(base, CONTEXT), CONTEXT));
    }
}
