package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.numeral.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.utils.RangeUtils.*;

/**
 * @author datafox
 */
public class ConversionUtils {
    public static int toInt(Numeral numeral) {
        try {
            switch(numeral.getType()) {
                case INT:
                    return (int) numeral.getNumber();
                case LONG:
                    return Math.toIntExact((long) numeral.getNumber());
                case BIG_INT:
                    return ((BigInteger) numeral.getNumber()).intValueExact();
                case FLOAT:
                    return toIntInRange((float) numeral.getNumber());
                case DOUBLE:
                    return toIntInRange((double) numeral.getNumber());
                case BIG_DEC:
                    return toIntInRange((BigDecimal) numeral.getNumber());
            }
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static long toLong(Numeral numeral) {
        try {
            switch(numeral.getType()) {
                case INT:
                case LONG:
                    return (long) numeral.getNumber();
                case BIG_INT:
                    return ((BigInteger) numeral.getNumber()).longValueExact();
                case FLOAT:
                    return toLongInRange((float) numeral.getNumber());
                case DOUBLE:
                    return toLongInRange((double) numeral.getNumber());
                case BIG_DEC:
                    return toLongInRange((BigDecimal) numeral.getNumber());
            }
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static BigInteger toBigInt(Numeral numeral) {
        try {
            switch(numeral.getType()) {
                case INT:
                    return BigInteger.valueOf((int) numeral.getNumber());
                case LONG:
                    return BigInteger.valueOf((long) numeral.getNumber());
                case BIG_INT:
                    return (BigInteger) numeral.getNumber();
                case FLOAT:
                    return BigDecimal.valueOf((float) numeral.getNumber()).toBigInteger();
                case DOUBLE:
                    return BigDecimal.valueOf((double) numeral.getNumber()).toBigInteger();
                case BIG_DEC:
                    return ((BigDecimal) numeral.getNumber()).toBigInteger();
            }
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static float toFloat(Numeral numeral) {
        try {
            switch(numeral.getType()) {
                case INT:
                case LONG:
                case FLOAT:
                    return (float) numeral.getNumber();
                case BIG_INT:
                    return toFloatInRange((BigInteger) numeral.getNumber());
                case DOUBLE:
                    return toFloatInRange((double) numeral.getNumber());
                case BIG_DEC:
                    return toFloatInRange((BigDecimal) numeral.getNumber());
            }
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static double toDouble(Numeral numeral) {
        try {
            switch(numeral.getType()) {
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                    return (double) numeral.getNumber();
                case BIG_INT:
                    return toDoubleInRange((BigInteger) numeral.getNumber());
                case BIG_DEC:
                    return toDoubleInRange((BigDecimal) numeral.getNumber());
            }
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static BigDecimal toBigDec(Numeral numeral) {
        try {
            switch(numeral.getType()) {
                case INT:
                    return BigDecimal.valueOf((int) numeral.getNumber());
                case LONG:
                    return BigDecimal.valueOf((long) numeral.getNumber());
                case FLOAT:
                    return BigDecimal.valueOf((float) numeral.getNumber());
                case DOUBLE:
                    return BigDecimal.valueOf((double) numeral.getNumber());
                case BIG_INT:
                    return new BigDecimal((BigInteger) numeral.getNumber());
                case BIG_DEC:
                    return (BigDecimal) numeral.getNumber();
            }
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static Numeral toSmallestType(Numeral numeral) {
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
        throw new IllegalArgumentException("unknown type");
    }

    public static Numeral toDecimal(Numeral source) {
        switch(source.getType()) {
            case INT:
            case LONG:
                return toFloatNumeral(source);
            case BIG_INT:
                if(!isOutOfFloatRange(source)) {
                    return toFloatNumeral(source);
                }

                if(!isOutOfDoubleRange(source)) {
                    return toDoubleNumeral(source);
                }

                return toBigDecNumeral(source);
        }

        return source;
    }

    public static Numeral toNumeral(Numeral source, NumeralType type) {
        switch(type) {
            case INT:
                return toIntNumeral(source);
            case LONG:
                return toLongNumeral(source);
            case BIG_INT:
                return toBigIntNumeral(source);
            case FLOAT:
                return toFloatNumeral(source);
            case DOUBLE:
                return toDoubleNumeral(source);
            case BIG_DEC:
                return toBigDecNumeral(source);
        }
        throw new IllegalArgumentException("unknown type");
    }

    public static IntNumeral toNumeral(int i) {
        return new IntNumeral(i);
    }

    public static LongNumeral toNumeral(long l) {
        return new LongNumeral(l);
    }

    public static BigIntNumeral toNumeral(BigInteger bi) {
        return new BigIntNumeral(bi);
    }

    public static FloatNumeral toNumeral(float f) {
        return new FloatNumeral(f);
    }

    public static DoubleNumeral toNumeral(double d) {
        return new DoubleNumeral(d);
    }

    public static BigDecNumeral toNumeral(BigDecimal bd) {
        return new BigDecNumeral(bd);
    }

    public static IntNumeral toIntNumeral(Numeral numeral) {
        if(numeral instanceof IntNumeral) {
            return (IntNumeral) numeral;
        }

        return toNumeral(toInt(numeral));
    }

    public static LongNumeral toLongNumeral(Numeral numeral) {
        if(numeral instanceof LongNumeral) {
            return (LongNumeral) numeral;
        }

        return toNumeral(toLong(numeral));
    }

    public static BigIntNumeral toBigIntNumeral(Numeral numeral) {
        if(numeral instanceof BigIntNumeral) {
            return (BigIntNumeral) numeral;
        }

        return toNumeral(toBigInt(numeral));
    }

    public static FloatNumeral toFloatNumeral(Numeral numeral) {
        if(numeral instanceof FloatNumeral) {
            return (FloatNumeral) numeral;
        }

        return toNumeral(toFloat(numeral));
    }

    public static DoubleNumeral toDoubleNumeral(Numeral numeral) {
        if(numeral instanceof DoubleNumeral) {
            return (DoubleNumeral) numeral;
        }

        return toNumeral(toDouble(numeral));
    }

    public static BigDecNumeral toBigDecNumeral(Numeral numeral) {
        if(numeral instanceof BigDecNumeral) {
            return (BigDecNumeral) numeral;
        }

        return toNumeral(toBigDec(numeral));
    }

    public static int toIntInRange(float f) {
        if(!isOutOfIntRange(f)) {
            return (int) f;
        }

        throw new ArithmeticException("integer overflow");
    }

    public static int toIntInRange(double d) {
        if(!isOutOfIntRange(d)) {
            return (int) d;
        }

        throw new ArithmeticException("integer overflow");
    }

    public static int toIntInRange(BigDecimal bd) {
        if(!isOutOfIntRange(bd)) {
            return bd.intValue();
        }

        throw new ArithmeticException("integer overflow");
    }

    public static long toLongInRange(float f) {
        if(!isOutOfLongRange(f)) {
            return (long) f;
        }

        throw new ArithmeticException("integer overflow");
    }

    public static long toLongInRange(double d) {
        if(!isOutOfLongRange(d)) {
            return (long) d;
        }

        throw new ArithmeticException("integer overflow");
    }

    public static long toLongInRange(BigDecimal bd) {
        if(!isOutOfLongRange(bd)) {
            return bd.longValue();
        }

        throw new ArithmeticException("integer overflow");
    }

    public static float toFloatInRange(BigInteger bi) {
        return toFloatInRange(new BigDecimal(bi));
    }

    public static float toFloatInRange(double d) {
        if(!isOutOfFloatRange(d)) {
            return (float) d;
        }

        throw new ArithmeticException("floating point overflow");
    }

    private static float toFloatInRange(BigDecimal bd) {
        if(!isOutOfFloatRange(bd)) {
            return bd.floatValue();
        }

        throw new ArithmeticException("floating point overflow");
    }

    public static double toDoubleInRange(BigInteger bi) {
        return toDoubleInRange(new BigDecimal(bi));
    }

    private static double toDoubleInRange(BigDecimal bd) {
        if(!isOutOfDoubleRange(bd)) {
            return bd.doubleValue();
        }

        throw new ArithmeticException("floating point overflow");
    }
}
