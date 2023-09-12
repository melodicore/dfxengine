package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.numeral.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.api.NumeralType.BIG_DEC;

/**
 * @author datafox
 */
public class Numerals {
    public static IntNumeral valueOf(int i) {
        return new IntNumeral(i);
    }

    public static LongNumeral valueOf(long l) {
        return new LongNumeral(l);
    }

    public static BigIntNumeral valueOf(BigInteger bi) {
        return new BigIntNumeral(bi);
    }

    public static FloatNumeral valueOf(float f) {
        return new FloatNumeral(f);
    }

    public static DoubleNumeral valueOf(double d) {
        return new DoubleNumeral(d);
    }

    public static BigDecNumeral valueOf(BigDecimal bd) {
        return new BigDecNumeral(bd);
    }

    public static Numeral valueOf(String str) {
        BigDecimal bd = new BigDecimal(str);
        if(bd.scale() <= 0 || bd.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            return new BigIntNumeral(bd.toBigInteger());
        }
        return new BigDecNumeral(bd);
    }

    public static boolean isZero(Numeral numeral) {
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
        throw new IllegalArgumentException("unknown type");
    }

    public static boolean isOne(Numeral numeral) {
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
        throw new IllegalArgumentException("unknown type");
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
        throw new IllegalArgumentException("unknown type");
    }public static NumeralType getSignificantType(Numeral numeral1, Numeral numeral2) {
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
