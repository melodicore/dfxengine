package me.datafox.dfxengine.math.test;

import me.datafox.dfxengine.math.numeral.*;
import me.datafox.dfxengine.math.utils.Numerals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.api.NumeralType.*;

/**
 * @author datafox
 */
public class NumeralsTest {
    @Test
    public void valueOfTest() {
        Assertions.assertInstanceOf(IntNumeral.class, Numerals.valueOf(72));
        Assertions.assertInstanceOf(LongNumeral.class, Numerals.valueOf(9524783641L));
        Assertions.assertInstanceOf(BigIntNumeral.class, Numerals.valueOf(new BigInteger("864208642086420864208642086420")));
        Assertions.assertInstanceOf(FloatNumeral.class, Numerals.valueOf(11.54f));
        Assertions.assertInstanceOf(DoubleNumeral.class, Numerals.valueOf(7.24e190d));
        Assertions.assertInstanceOf(BigDecNumeral.class, Numerals.valueOf(new BigDecimal("5.34812375923e+360")));

        Assertions.assertInstanceOf(BigIntNumeral.class, Numerals.valueOf("53462"));
        Assertions.assertInstanceOf(BigDecNumeral.class, Numerals.valueOf("53462e+1"));
        Assertions.assertInstanceOf(BigDecNumeral.class, Numerals.valueOf("5.3462e+3"));
        Assertions.assertThrows(NumberFormatException.class, () -> Numerals.valueOf("941.76.245"));
        Assertions.assertThrows(NumberFormatException.class, () -> Numerals.valueOf("941,76"));
        Assertions.assertThrows(NumberFormatException.class, () -> Numerals.valueOf("941a76"));
    }

    @Test
    public void isZeroTest() {
        Assertions.assertTrue(Numerals.isZero(new IntNumeral(0)));
        Assertions.assertTrue(Numerals.isZero(new LongNumeral(0L)));
        Assertions.assertTrue(Numerals.isZero(new BigIntNumeral("0")));
        Assertions.assertTrue(Numerals.isZero(new FloatNumeral(0.0f)));
        Assertions.assertTrue(Numerals.isZero(new FloatNumeral(-0.0f)));
        Assertions.assertTrue(Numerals.isZero(new DoubleNumeral(0.0d)));
        Assertions.assertTrue(Numerals.isZero(new DoubleNumeral(-0.0d)));
        Assertions.assertTrue(Numerals.isZero(new BigDecNumeral(0)));
        Assertions.assertTrue(Numerals.isZero(new BigDecNumeral("0.0")));

        Assertions.assertFalse(Numerals.isZero(new IntNumeral(7)));
        Assertions.assertFalse(Numerals.isZero(new LongNumeral(-345345345345345L)));
        Assertions.assertFalse(Numerals.isZero(new BigIntNumeral(37)));
        Assertions.assertFalse(Numerals.isZero(new FloatNumeral(0.1f)));
        Assertions.assertFalse(Numerals.isZero(new FloatNumeral(-Float.MIN_VALUE)));
        Assertions.assertFalse(Numerals.isZero(new DoubleNumeral(94.7d)));
        Assertions.assertFalse(Numerals.isZero(new BigDecNumeral("-2.5147")));
        Assertions.assertFalse(Numerals.isZero(new BigDecNumeral("1e-734")));
    }

    @Test
    public void isOneTest() {
        Assertions.assertTrue(Numerals.isOne(new IntNumeral(1)));
        Assertions.assertTrue(Numerals.isOne(new LongNumeral(1L)));
        Assertions.assertTrue(Numerals.isOne(new BigIntNumeral(1)));
        Assertions.assertTrue(Numerals.isOne(new FloatNumeral(1.0f)));
        Assertions.assertTrue(Numerals.isOne(new DoubleNumeral(1.0d)));
        Assertions.assertTrue(Numerals.isOne(new BigDecNumeral(1)));
        Assertions.assertTrue(Numerals.isOne(new BigDecNumeral("1.0")));

        Assertions.assertFalse(Numerals.isOne(new IntNumeral(7)));
        Assertions.assertFalse(Numerals.isOne(new LongNumeral(-345345345345345L)));
        Assertions.assertFalse(Numerals.isOne(new BigIntNumeral(37)));
        Assertions.assertFalse(Numerals.isOne(new FloatNumeral(0.1f)));
        Assertions.assertFalse(Numerals.isOne(new FloatNumeral(-Float.MIN_VALUE)));
        Assertions.assertFalse(Numerals.isOne(new DoubleNumeral(94.7d)));
        Assertions.assertFalse(Numerals.isOne(new BigDecNumeral("-2.5147")));
        Assertions.assertFalse(Numerals.isOne(new BigDecNumeral("1e-734")));
    }

    @Test
    public void compareTest() {
        Assertions.assertEquals(0, Numerals.compare(new IntNumeral(72), new BigIntNumeral(72)));
        Assertions.assertEquals(0, Numerals.compare(new FloatNumeral(0.4f), new DoubleNumeral(0.4d)));
        Assertions.assertEquals(0, Numerals.compare(new FloatNumeral(9372.561f), new BigDecNumeral("9372.561")));
        Assertions.assertEquals(1, Numerals.compare(new BigDecNumeral("69247.0000000000001"), new IntNumeral(69247)));
        Assertions.assertEquals(-1, Numerals.compare(new DoubleNumeral(-0.0), new DoubleNumeral(0.0)));
    }

    @Test
    public void getSignificantTypeTest() {
        Assertions.assertEquals(INT, Numerals.getSignificantType(INT, INT));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(INT, LONG));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(LONG, LONG));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(INT, BIG_INT));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(LONG, BIG_INT));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(BIG_INT, BIG_INT));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(INT, FLOAT));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(LONG, FLOAT));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(FLOAT, FLOAT));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(INT, DOUBLE));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(LONG, DOUBLE));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(FLOAT, DOUBLE));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(DOUBLE, DOUBLE));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(INT, BIG_DEC));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(LONG, BIG_DEC));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(BIG_INT, BIG_DEC));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(FLOAT, BIG_DEC));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(DOUBLE, BIG_DEC));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(BIG_DEC, BIG_DEC));

        Assertions.assertEquals(INT, Numerals.getSignificantType(INT, INT, INT));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(INT, LONG, INT));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(LONG, LONG, INT));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(INT, BIG_INT, INT));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(LONG, BIG_INT, INT));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(BIG_INT, BIG_INT, INT));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(INT, FLOAT, INT, LONG));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(LONG, FLOAT, INT, LONG));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(FLOAT, FLOAT, INT, LONG));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(INT, DOUBLE, INT, LONG, FLOAT));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(LONG, DOUBLE, INT, LONG, FLOAT));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(FLOAT, DOUBLE, INT, LONG, FLOAT));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(DOUBLE, DOUBLE, INT, LONG, FLOAT));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(INT, BIG_DEC, INT, LONG, BIG_INT, FLOAT, DOUBLE));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(LONG, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(BIG_INT, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(FLOAT, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(DOUBLE, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(BIG_DEC, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
    }
}
