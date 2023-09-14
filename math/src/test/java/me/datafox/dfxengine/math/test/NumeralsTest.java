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
        Assertions.assertInstanceOf(BigIntNumeral.class, Numerals.valueOf("5.3462e+4"));
        Assertions.assertInstanceOf(BigDecNumeral.class, Numerals.valueOf("5.3462e+3"));
    }

    @Test
    public void isZeroTest() {
        Assertions.assertTrue(Numerals.isZero(new IntNumeral(0)));
        Assertions.assertTrue(Numerals.isZero(new LongNumeral(0L)));
        Assertions.assertTrue(Numerals.isZero(new BigIntNumeral(new BigInteger("0"))));
        Assertions.assertTrue(Numerals.isZero(new FloatNumeral(0.0f)));
        Assertions.assertTrue(Numerals.isZero(new FloatNumeral(-0.0f)));
        Assertions.assertTrue(Numerals.isZero(new DoubleNumeral(0.0d)));
        Assertions.assertTrue(Numerals.isZero(new DoubleNumeral(-0.0d)));
        Assertions.assertTrue(Numerals.isZero(new BigDecNumeral(new BigDecimal("0"))));
        Assertions.assertTrue(Numerals.isZero(new BigDecNumeral(new BigDecimal("0.0"))));

        Assertions.assertFalse(Numerals.isZero(new IntNumeral(7)));
        Assertions.assertFalse(Numerals.isZero(new LongNumeral(-345345345345345L)));
        Assertions.assertFalse(Numerals.isZero(new BigIntNumeral(new BigInteger("37"))));
        Assertions.assertFalse(Numerals.isZero(new FloatNumeral(0.1f)));
        Assertions.assertFalse(Numerals.isZero(new FloatNumeral(-Float.MIN_VALUE)));
        Assertions.assertFalse(Numerals.isZero(new DoubleNumeral(94.7d)));
        Assertions.assertFalse(Numerals.isZero(new BigDecNumeral(new BigDecimal("-2.5147"))));
        Assertions.assertFalse(Numerals.isZero(new BigDecNumeral(new BigDecimal("1e-734"))));
    }

    @Test
    public void isOneTest() {
        Assertions.assertTrue(Numerals.isOne(new IntNumeral(1)));
        Assertions.assertTrue(Numerals.isOne(new LongNumeral(1L)));
        Assertions.assertTrue(Numerals.isOne(new BigIntNumeral(new BigInteger("1"))));
        Assertions.assertTrue(Numerals.isOne(new FloatNumeral(1.0f)));
        Assertions.assertTrue(Numerals.isOne(new DoubleNumeral(1.0d)));
        Assertions.assertTrue(Numerals.isOne(new BigDecNumeral(new BigDecimal("1"))));
        Assertions.assertTrue(Numerals.isOne(new BigDecNumeral(new BigDecimal("1.0"))));

        Assertions.assertFalse(Numerals.isOne(new IntNumeral(7)));
        Assertions.assertFalse(Numerals.isOne(new LongNumeral(-345345345345345L)));
        Assertions.assertFalse(Numerals.isOne(new BigIntNumeral(new BigInteger("37"))));
        Assertions.assertFalse(Numerals.isOne(new FloatNumeral(0.1f)));
        Assertions.assertFalse(Numerals.isOne(new FloatNumeral(-Float.MIN_VALUE)));
        Assertions.assertFalse(Numerals.isOne(new DoubleNumeral(94.7d)));
        Assertions.assertFalse(Numerals.isOne(new BigDecNumeral(new BigDecimal("-2.5147"))));
        Assertions.assertFalse(Numerals.isOne(new BigDecNumeral(new BigDecimal("1e-734"))));
    }

    @Test
    public void compareTest() {
        Assertions.assertEquals(0, Numerals.compare(new IntNumeral(72), new BigIntNumeral(new BigInteger("72"))));
        Assertions.assertEquals(0, Numerals.compare(new FloatNumeral(0.4f), new DoubleNumeral(0.4d)));
        Assertions.assertEquals(0, Numerals.compare(new FloatNumeral(9372.561f), new BigDecNumeral(new BigDecimal("9372.561"))));
        Assertions.assertEquals(1, Numerals.compare(new BigDecNumeral(new BigDecimal("69247.0000000000001")), new IntNumeral(69247)));
        Assertions.assertEquals(-1, Numerals.compare(new DoubleNumeral(-0.0), new DoubleNumeral(0.0)));
    }

    @Test
    public void getSignificantTypeTest() {
        IntNumeral i = new IntNumeral(0);
        LongNumeral l = new LongNumeral(0);
        BigIntNumeral bi = new BigIntNumeral(BigInteger.ZERO);
        FloatNumeral f = new FloatNumeral(0);
        DoubleNumeral d = new DoubleNumeral(0);
        BigDecNumeral bd = new BigDecNumeral(BigDecimal.ZERO);

        Assertions.assertEquals(INT, Numerals.getSignificantType(i, i));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(i, l));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(l, l));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(i, bi));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(l, bi));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(bi, bi));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(i, f));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(l, f));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(f, f));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(i, d));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(l, d));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(f, d));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(d, d));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(i, bd));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(l, bd));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(bi, bd));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(f, bd));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(d, bd));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(bd, bd));

        Assertions.assertEquals(INT, Numerals.getSignificantType(i, i, i));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(i, l, i));
        Assertions.assertEquals(LONG, Numerals.getSignificantType(l, l, i));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(i, bi, i));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(l, bi, i));
        Assertions.assertEquals(BIG_INT, Numerals.getSignificantType(bi, bi, i));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(i, f, i, l));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(l, f, i, l));
        Assertions.assertEquals(FLOAT, Numerals.getSignificantType(f, f, i, l));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(i, d, i, l, f));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(l, d, i, l, f));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(f, d, i, l, f));
        Assertions.assertEquals(DOUBLE, Numerals.getSignificantType(d, d, i, l, f));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(i, bd, i, l, bi, f, d));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(l, bd, i, bi, f, d));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(bi, bd, i, bi, f, d));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(f, bd, i, bi, f, d));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(d, bd, i, bi, f, d));
        Assertions.assertEquals(BIG_DEC, Numerals.getSignificantType(bd, bd, i, bi, f, d));
    }
}
