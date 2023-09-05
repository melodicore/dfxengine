package me.datafox.dfxengine.math.utils;

import me.datafox.dfxengine.math.numeral.*;

import java.math.BigDecimal;
import java.math.BigInteger;

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
}
