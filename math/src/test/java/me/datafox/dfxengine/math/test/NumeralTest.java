package me.datafox.dfxengine.math.test;

import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.numeral.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.api.NumeralType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class NumeralTest {
    @Test
    public void intNumeralTest() {
        IntNumeral numeral = new IntNumeral(93);

        assertInstanceOf(Integer.class, numeral.getNumber());
        assertEquals(INT, numeral.getType());
        assertEquals(LONG, numeral.convert(LONG).getType());
        assertEquals(DOUBLE, numeral.convertIfAllowed(DOUBLE).getType());
        assertEquals(FLOAT, numeral.convertToDecimal().getType());
        assertTrue(numeral.canConvert(BIG_INT));
        assertEquals(numeral, numeral.toSmallestType());

        assertEquals(93, numeral.intValue());
        assertEquals(93L, numeral.longValue());
        assertEquals(BigInteger.valueOf(93), numeral.bigIntValue());
        assertEquals(93.0f, numeral.floatValue());
        assertEquals(93.0d, numeral.doubleValue());
        assertEquals(BigDecimal.valueOf(93), numeral.bigDecValue());
    }

    @Test
    public void longNumeralTest() {
        //Long larger than Integer.MAX_VALUE
        LongNumeral numeral = new LongNumeral(36214209001609L);

        assertInstanceOf(Long.class, numeral.getNumber());
        assertEquals(LONG, numeral.getType());
        assertEquals(FLOAT, numeral.convert(FLOAT).getType());
        assertThrows(ExtendedArithmeticException.class, () -> numeral.convert(INT));
        assertEquals(DOUBLE, numeral.convertIfAllowed(DOUBLE).getType());
        assertEquals(numeral, numeral.convertIfAllowed(INT));
        assertTrue(numeral.canConvert(BIG_DEC));
        assertFalse(numeral.canConvert(INT));

        assertThrows(ExtendedArithmeticException.class, numeral::intValue);
        assertEquals(36214209001609L, numeral.longValue());
        assertEquals(BigInteger.valueOf(36214209001609L), numeral.bigIntValue());
        assertEquals(3.6214208e13f, numeral.floatValue());
        assertEquals(3.6214209001609e13d, numeral.doubleValue());
        assertEquals(BigDecimal.valueOf(36214209001609L), numeral.bigDecValue());

        LongNumeral smallNumeral = new LongNumeral(455);
        assertEquals(INT, smallNumeral.toSmallestType().getType());
    }

    @Test
    public void bigIntNumeralTest() {
        //BigInteger larger than Long.MAX_VALUE
        BigIntNumeral numeral = new BigIntNumeral(BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(5)));

        assertInstanceOf(BigInteger.class, numeral.getNumber());
        assertEquals(BIG_INT, numeral.getType());
        assertEquals(FLOAT, numeral.convert(FLOAT).getType());
        assertThrows(ExtendedArithmeticException.class, () -> numeral.convert(LONG));
        assertEquals(DOUBLE, numeral.convertIfAllowed(DOUBLE).getType());
        assertEquals(numeral, numeral.convertIfAllowed(LONG));
        assertTrue(numeral.canConvert(DOUBLE));
        assertFalse(numeral.canConvert(INT));

        assertThrows(ExtendedArithmeticException.class, numeral::intValue);
        assertThrows(ExtendedArithmeticException.class, numeral::longValue);
        assertEquals(new BigInteger("46116860184273879035"), numeral.bigIntValue());
        assertEquals(4.611686e19f, numeral.floatValue());
        assertEquals(4.611686018427388e19d, numeral.doubleValue());
        assertEquals(new BigDecimal("46116860184273879035"), numeral.bigDecValue());

        BigIntNumeral smallNumeral = new BigIntNumeral(BigInteger.valueOf(36214209001609L));
        assertEquals(LONG, smallNumeral.toSmallestType().getType());
    }

    @Test
    public void floatNumeralTest() {
        //BigInteger larger than Long.MAX_VALUE
        FloatNumeral numeral = new FloatNumeral(4e20f);

        assertInstanceOf(Float.class, numeral.getNumber());
        assertEquals(FLOAT, numeral.getType());
        assertEquals(DOUBLE, numeral.convert(DOUBLE).getType());
        assertThrows(ExtendedArithmeticException.class, () -> numeral.convert(LONG));
        assertEquals(BIG_DEC, numeral.convertIfAllowed(BIG_DEC).getType());
        assertEquals(numeral, numeral.convertIfAllowed(INT));
        assertTrue(numeral.canConvert(DOUBLE));
        assertFalse(numeral.canConvert(LONG));

        assertThrows(ExtendedArithmeticException.class, numeral::intValue);
        assertThrows(ExtendedArithmeticException.class, numeral::longValue);
        assertEquals(new BigInteger("400000000000000000000"), numeral.bigIntValue());
        assertEquals(4.0e20f, numeral.floatValue());
        assertEquals(4.0e20d, numeral.doubleValue());
        assertEquals(new BigDecimal("4.0e+20"), numeral.bigDecValue());

        assertThrows(IllegalArgumentException.class, () -> new FloatNumeral(Float.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new FloatNumeral(Float.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new FloatNumeral(Float.NaN));
    }

    @Test
    public void doubleNumeralTest() {
        //BigInteger larger than Float.MAX_VALUE
        DoubleNumeral numeral = new DoubleNumeral(4.2e69d);

        assertInstanceOf(Double.class, numeral.getNumber());
        assertEquals(DOUBLE, numeral.getType());
        assertEquals(BIG_INT, numeral.convert(BIG_INT).getType());
        assertThrows(ExtendedArithmeticException.class, () -> numeral.convert(FLOAT));
        assertEquals(BIG_DEC, numeral.convertIfAllowed(BIG_DEC).getType());
        assertEquals(numeral, numeral.convertIfAllowed(INT));
        assertTrue(numeral.canConvert(BIG_INT));
        assertFalse(numeral.canConvert(INT));

        assertThrows(ExtendedArithmeticException.class, numeral::intValue);
        assertThrows(ExtendedArithmeticException.class, numeral::longValue);
        assertEquals(new BigInteger("4200000000000000000000000000000000000000000000000000000000000000000000"), numeral.bigIntValue());
        assertThrows(ExtendedArithmeticException.class, numeral::floatValue);
        assertEquals(4.2e69d, numeral.doubleValue());
        assertEquals(new BigDecimal("4.2e+69"), numeral.bigDecValue());

        DoubleNumeral smallNumeral = new DoubleNumeral(1e16d);
        assertEquals(FLOAT, smallNumeral.toSmallestType().getType());

        assertThrows(IllegalArgumentException.class, () -> new DoubleNumeral(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new DoubleNumeral(Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new DoubleNumeral(Double.NaN));
    }

    @Test
    public void bigDecNumeralTest() {
        //BigInteger larger than Double.MAX_VALUE
        BigDecNumeral numeral = new BigDecNumeral(BigDecimal.valueOf(Double.MAX_VALUE).multiply(BigDecimal.valueOf(5)));

        assertInstanceOf(BigDecimal.class, numeral.getNumber());
        assertEquals(BIG_DEC, numeral.getType());
        assertEquals(BIG_INT, numeral.convert(BIG_INT).getType());
        assertThrows(ExtendedArithmeticException.class, () -> numeral.convert(DOUBLE));
        assertEquals(BIG_INT, numeral.convertIfAllowed(BIG_INT).getType());
        assertEquals(numeral, numeral.convertIfAllowed(INT));
        assertTrue(numeral.canConvert(BIG_DEC));
        assertFalse(numeral.canConvert(FLOAT));

        assertThrows(ExtendedArithmeticException.class, numeral::intValue);
        assertThrows(ExtendedArithmeticException.class, numeral::longValue);
        assertEquals(new BigInteger("898846567431157850000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), numeral.bigIntValue());
        assertThrows(ExtendedArithmeticException.class, numeral::floatValue);
        assertThrows(ExtendedArithmeticException.class, numeral::doubleValue);
        assertEquals(new BigDecimal("8.9884656743115785e+308"), numeral.bigDecValue());

        BigDecNumeral smallNumeral = new BigDecNumeral(BigDecimal.valueOf(9.37e123));
        assertEquals(DOUBLE, smallNumeral.toSmallestType().getType());
    }
}
