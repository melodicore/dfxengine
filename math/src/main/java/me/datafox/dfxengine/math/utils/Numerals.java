package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.numeral.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.api.NumeralType.BIG_DEC;
import static me.datafox.dfxengine.math.api.NumeralType.BIG_INT;

/**
 * Various operations for {@link Numeral Numerals}.
 *
 * @author datafox
 */
public class Numerals {
    /**
     * @param i {@code int} value
     * @return {@link IntNumeral} representing specified value
     */
    public static IntNumeral valueOf(int i) {
        return new IntNumeral(i);
    }

    /**
     * @param l {@code long} value
     * @return {@link LongNumeral} representing specified value
     */
    public static LongNumeral valueOf(long l) {
        return new LongNumeral(l);
    }

    /**
     * @param bi {@link BigInteger} value
     * @return {@link BigIntNumeral} representing specified value
     */
    public static BigIntNumeral valueOf(BigInteger bi) {
        return new BigIntNumeral(bi);
    }

    /**
     * @param f {@code float} value
     * @return {@link FloatNumeral} representing specified value
     */
    public static FloatNumeral valueOf(float f) {
        return new FloatNumeral(f);
    }

    /**
     * @param d {@code double} value
     * @return {@link DoubleNumeral} representing specified value
     */
    public static DoubleNumeral valueOf(double d) {
        return new DoubleNumeral(d);
    }

    /**
     * @param bd {@link BigDecimal} value
     * @return {@link BigDecNumeral} representing specified value
     */
    public static BigDecNumeral valueOf(BigDecimal bd) {
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
    public static Numeral valueOf(String str) {
        if(str.matches(".*[.eE].*")) {
            return valueOf(new BigDecimal(str));
        } else {
            return valueOf(new BigInteger(str));
        }
    }

    /**
     * @param numeral {@link Numeral} value
     * @return {@code true} if the specified value represents the number zero
     */
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


    /**
     * @param numeral {@link Numeral} value
     * @return {@code true} if the specified value represents the number one
     */
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

    /**
     * @param numeral first {@link Numeral} to compare
     * @param other second {@link Numeral} to compare
     * @return 0 if the {@link Numeral} values represent the same number, 1 if the first Numeral represents a larger
     * value than the second Numeral, and -1 if the first Numeral represents a smaller value than the second Numeral.
     */
    public static int compare(Numeral numeral, Numeral other) {
        switch(getSignificantType(numeral.getType(), other.getType())) {
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
    }

    /**
     * @param type1 first {@link NumeralType}
     * @param type2 second {@link NumeralType}
     * @return the most significant type of specified values
     *
     * @see Numerals#getSignificantType(NumeralType...)
     */
    public static NumeralType getSignificantType(NumeralType type1, NumeralType type2) {
        if(type1.equals(type2)) {
            return type1;
        }

        return getSignificantType(new NumeralType[] { type1, type2 });
    }

    /**
     * <p>
     * Returns the most significant {@link NumeralType}. The significance of a type is determined by how large numerical
     * values it can represent. Additionally, if any of the specified types is a
     * {@link NumeralType#isDecimal() decimal type}, the return value of this method is also a decimal type.
     * </p>
     * <p>
     * Therefore, if any of the specified types is {@link NumeralType#BIG_DEC BIG_DEC}, BIG_DEC is also returned.
     * BIG_DEC is also returned if any of the specified types is {@link NumeralType#BIG_INT BIG_INT} and another is
     * a decimal type. In all other cases the specified type with the highest
     * {@link NumeralType#getSignificance() significance} will be returned.
     * </p>
     * @param types types to be checked
     * @return the most significant type
     */
    public static NumeralType getSignificantType(NumeralType ... types) {
        if(types.length == 0) {
            throw new IllegalArgumentException("empty array");
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
}
