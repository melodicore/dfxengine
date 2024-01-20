package me.datafox.dfxengine.math.test;

import me.datafox.dfxengine.math.numeral.*;
import me.datafox.dfxengine.math.utils.Numerals;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.api.NumeralType.*;
import static me.datafox.dfxengine.math.utils.Numerals.of;
import static me.datafox.dfxengine.math.utils.Numerals.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class NumeralsTest {
    @Test
    public void valueOfTest() {
        assertInstanceOf(IntNumeral.class, Numerals.of(72));
        assertInstanceOf(LongNumeral.class, Numerals.of(9524783641L));
        assertInstanceOf(BigIntNumeral.class, Numerals.of(new BigInteger("864208642086420864208642086420")));
        assertInstanceOf(FloatNumeral.class, Numerals.of(11.54f));
        assertInstanceOf(DoubleNumeral.class, Numerals.of(7.24e190d));
        assertInstanceOf(BigDecNumeral.class, Numerals.of(new BigDecimal("5.34812375923e+360")));

        assertInstanceOf(BigIntNumeral.class, Numerals.of("53462"));
        assertInstanceOf(BigDecNumeral.class, Numerals.of("53462e+1"));
        assertInstanceOf(BigDecNumeral.class, Numerals.of("5.3462e+3"));
        assertThrows(NumberFormatException.class, () -> Numerals.of("941.76.245"));
        assertThrows(NumberFormatException.class, () -> Numerals.of("941,76"));
        assertThrows(NumberFormatException.class, () -> Numerals.of("941a76"));

        assertInstanceOf(LongNumeral.class, of(Long.valueOf(1337L)));
        assertThrows(IllegalArgumentException.class, () -> of(Short.valueOf("37")));
    }

    @Test
    public void isZeroTest() {
        assertTrue(isZero(new IntNumeral(0)));
        assertTrue(isZero(new LongNumeral(0L)));
        assertTrue(isZero(new BigIntNumeral("0")));
        assertTrue(isZero(new FloatNumeral(0.0f)));
        assertTrue(isZero(new FloatNumeral(-0.0f)));
        assertTrue(isZero(new DoubleNumeral(0.0d)));
        assertTrue(isZero(new DoubleNumeral(-0.0d)));
        assertTrue(isZero(new BigDecNumeral(0)));
        assertTrue(isZero(new BigDecNumeral("0.0")));

        assertFalse(isZero(new IntNumeral(7)));
        assertFalse(isZero(new LongNumeral(-345345345345345L)));
        assertFalse(isZero(new BigIntNumeral(37)));
        assertFalse(isZero(new FloatNumeral(0.1f)));
        assertFalse(isZero(new FloatNumeral(-Float.MIN_VALUE)));
        assertFalse(isZero(new DoubleNumeral(94.7d)));
        assertFalse(isZero(new BigDecNumeral("-2.5147")));
        assertFalse(isZero(new BigDecNumeral("1e-734")));
    }

    @Test
    public void isOneTest() {
        assertTrue(isOne(new IntNumeral(1)));
        assertTrue(isOne(new LongNumeral(1L)));
        assertTrue(isOne(new BigIntNumeral(1)));
        assertTrue(isOne(new FloatNumeral(1.0f)));
        assertTrue(isOne(new DoubleNumeral(1.0d)));
        assertTrue(isOne(new BigDecNumeral(1)));
        assertTrue(isOne(new BigDecNumeral("1.0")));

        assertFalse(isOne(new IntNumeral(7)));
        assertFalse(isOne(new LongNumeral(-345345345345345L)));
        assertFalse(isOne(new BigIntNumeral(37)));
        assertFalse(isOne(new FloatNumeral(0.1f)));
        assertFalse(isOne(new FloatNumeral(-Float.MIN_VALUE)));
        assertFalse(isOne(new DoubleNumeral(94.7d)));
        assertFalse(isOne(new BigDecNumeral("-2.5147")));
        assertFalse(isOne(new BigDecNumeral("1e-734")));
    }

    @Test
    public void compareTest() {
        assertEquals(0, compare(new IntNumeral(72), new BigIntNumeral(72)));
        assertEquals(0, compare(new FloatNumeral(0.4f), new DoubleNumeral(0.4d)));
        assertEquals(0, compare(new FloatNumeral(9372.561f), new BigDecNumeral("9372.561")));
        assertEquals(1, compare(new BigDecNumeral("69247.0000000000001"), new IntNumeral(69247)));
        assertEquals(-1, compare(new DoubleNumeral(-0.0), new DoubleNumeral(0.0)));
    }

    @Test
    public void getSignificantTypeTest() {
        assertEquals(INT, getSignificantType(INT, INT));
        assertEquals(LONG, getSignificantType(INT, LONG));
        assertEquals(LONG, getSignificantType(LONG, LONG));
        assertEquals(BIG_INT, getSignificantType(INT, BIG_INT));
        assertEquals(BIG_INT, getSignificantType(LONG, BIG_INT));
        assertEquals(BIG_INT, getSignificantType(BIG_INT, BIG_INT));
        assertEquals(FLOAT, getSignificantType(INT, FLOAT));
        assertEquals(FLOAT, getSignificantType(LONG, FLOAT));
        assertEquals(FLOAT, getSignificantType(FLOAT, FLOAT));
        assertEquals(DOUBLE, getSignificantType(INT, DOUBLE));
        assertEquals(DOUBLE, getSignificantType(LONG, DOUBLE));
        assertEquals(DOUBLE, getSignificantType(FLOAT, DOUBLE));
        assertEquals(DOUBLE, getSignificantType(DOUBLE, DOUBLE));
        assertEquals(BIG_DEC, getSignificantType(INT, BIG_DEC));
        assertEquals(BIG_DEC, getSignificantType(LONG, BIG_DEC));
        assertEquals(BIG_DEC, getSignificantType(BIG_INT, BIG_DEC));
        assertEquals(BIG_DEC, getSignificantType(FLOAT, BIG_DEC));
        assertEquals(BIG_DEC, getSignificantType(DOUBLE, BIG_DEC));
        assertEquals(BIG_DEC, getSignificantType(BIG_DEC, BIG_DEC));

        assertEquals(INT, getSignificantType(INT, INT, INT));
        assertEquals(LONG, getSignificantType(INT, LONG, INT));
        assertEquals(LONG, getSignificantType(LONG, LONG, INT));
        assertEquals(BIG_INT, getSignificantType(INT, BIG_INT, INT));
        assertEquals(BIG_INT, getSignificantType(LONG, BIG_INT, INT));
        assertEquals(BIG_INT, getSignificantType(BIG_INT, BIG_INT, INT));
        assertEquals(FLOAT, getSignificantType(INT, FLOAT, INT, LONG));
        assertEquals(FLOAT, getSignificantType(LONG, FLOAT, INT, LONG));
        assertEquals(FLOAT, getSignificantType(FLOAT, FLOAT, INT, LONG));
        assertEquals(DOUBLE, getSignificantType(INT, DOUBLE, INT, LONG, FLOAT));
        assertEquals(DOUBLE, getSignificantType(LONG, DOUBLE, INT, LONG, FLOAT));
        assertEquals(DOUBLE, getSignificantType(FLOAT, DOUBLE, INT, LONG, FLOAT));
        assertEquals(DOUBLE, getSignificantType(DOUBLE, DOUBLE, INT, LONG, FLOAT));
        assertEquals(BIG_DEC, getSignificantType(INT, BIG_DEC, INT, LONG, BIG_INT, FLOAT, DOUBLE));
        assertEquals(BIG_DEC, getSignificantType(LONG, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        assertEquals(BIG_DEC, getSignificantType(BIG_INT, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        assertEquals(BIG_DEC, getSignificantType(FLOAT, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        assertEquals(BIG_DEC, getSignificantType(DOUBLE, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
        assertEquals(BIG_DEC, getSignificantType(BIG_DEC, BIG_DEC, INT, BIG_INT, FLOAT, DOUBLE));
    }
}
