package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.numeral.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.utils.Range.*;

/**
 * @author datafox
 */
public class Conversion {
    public static int toInt(Numeral numeral) {
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
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static long toLong(Numeral numeral) {
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
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static BigInteger toBigInt(Numeral numeral) {
        if(numeral instanceof BigIntNumeral || numeral instanceof FloatNumeral) {
            return numeral.bigIntValue();
        }

        try {
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
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static float toFloat(Numeral numeral) {
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
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static double toDouble(Numeral numeral) {
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
            throw new IllegalArgumentException("unknown type");
        } catch(ArithmeticException e) {
            throw new ArithmeticException("more description");
        }
    }

    public static BigDecimal toBigDec(Numeral numeral) {
        if(numeral instanceof BigDecNumeral || numeral instanceof FloatNumeral) {
            return numeral.bigDecValue();
        }

        try {
            switch(numeral.getType()) {
                case INT:
                case LONG:
                    return BigDecimal.valueOf(numeral.getNumber().longValue());
                case BIG_INT:
                    return new BigDecimal((BigInteger) numeral.getNumber());
                case FLOAT:
                case DOUBLE:
                    return BigDecimal.valueOf(numeral.getNumber().doubleValue());
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

    public static Numeral toDecimal(Numeral numeral) {
        switch(numeral.getType()) {
            case INT:
            case LONG:
                return toFloatNumeral(numeral);
            case BIG_INT:
                if(!isOutOfFloatRange(numeral)) {
                    return toFloatNumeral(numeral);
                }

                if(!isOutOfDoubleRange(numeral)) {
                    return toDoubleNumeral(numeral);
                }

                return toBigDecNumeral(numeral);
        }

        return numeral;
    }

    public static Numeral toNumeral(Numeral numeral, NumeralType type) {
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
        throw new IllegalArgumentException("unknown type");
    }

    public static IntNumeral toIntNumeral(Numeral numeral) {
        if(numeral instanceof IntNumeral) {
            return (IntNumeral) numeral;
        }

        return Numerals.valueOf(toInt(numeral));
    }

    public static LongNumeral toLongNumeral(Numeral numeral) {
        if(numeral instanceof LongNumeral) {
            return (LongNumeral) numeral;
        }

        return Numerals.valueOf(toLong(numeral));
    }

    public static BigIntNumeral toBigIntNumeral(Numeral numeral) {
        if(numeral instanceof BigIntNumeral) {
            return (BigIntNumeral) numeral;
        }

        return Numerals.valueOf(toBigInt(numeral));
    }

    public static FloatNumeral toFloatNumeral(Numeral numeral) {
        if(numeral instanceof FloatNumeral) {
            return (FloatNumeral) numeral;
        }

        return Numerals.valueOf(toFloat(numeral));
    }

    public static DoubleNumeral toDoubleNumeral(Numeral numeral) {
        if(numeral instanceof DoubleNumeral) {
            return (DoubleNumeral) numeral;
        }

        return Numerals.valueOf(toDouble(numeral));
    }

    public static BigDecNumeral toBigDecNumeral(Numeral numeral) {
        if(numeral instanceof BigDecNumeral) {
            return (BigDecNumeral) numeral;
        }

        return Numerals.valueOf(toBigDec(numeral));
    }

    private static int toIntInRange(float f) {
        if(isOutOfIntRange(f)) {
            throw new ArithmeticException("integer overflow");
        }

        return (int) f;
    }

    private static int toIntInRange(double d) {
        if(isOutOfIntRange(d)) {
            throw new ArithmeticException("integer overflow");
        }

        return (int) d;
    }

    private static int toIntInRange(BigDecimal bd) {
        if(isOutOfIntRange(bd)) {
            throw new ArithmeticException("integer overflow");
        }

        return bd.intValue();
    }

    private static long toLongInRange(float f) {
        if(isOutOfLongRange(f)) {
            throw new ArithmeticException("integer overflow");
        }

        return (long) f;
    }

    private static long toLongInRange(double d) {
        if(isOutOfLongRange(d)) {
            throw new ArithmeticException("integer overflow");
        }

        return (long) d;
    }

    private static long toLongInRange(BigDecimal bd) {
        if(isOutOfLongRange(bd)) {
            throw new ArithmeticException("integer overflow");
        }

        return bd.longValue();
    }

    private static float toFloatInRange(BigInteger bi) {
        return toFloatInRange(new BigDecimal(bi));
    }

    private static float toFloatInRange(double d) {
        if(isOutOfFloatRange(d)) {
            throw new ArithmeticException("floating point overflow");
        }

        return (float) d;
    }

    private static float toFloatInRange(BigDecimal bd) {
        if(isOutOfFloatRange(bd)) {
            throw new ArithmeticException("floating point overflow");
        }

        return bd.floatValue();
    }

    private static double toDoubleInRange(BigInteger bi) {
        return toDoubleInRange(new BigDecimal(bi));
    }

    private static double toDoubleInRange(BigDecimal bd) {
        if(isOutOfDoubleRange(bd)) {
            throw new ArithmeticException("floating point overflow");
        }

        return bd.doubleValue();
    }
}
