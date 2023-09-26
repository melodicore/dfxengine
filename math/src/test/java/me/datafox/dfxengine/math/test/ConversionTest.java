package me.datafox.dfxengine.math.test;

import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.numeral.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.api.NumeralType.*;
import static me.datafox.dfxengine.math.utils.Conversion.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class ConversionTest {
    private static final IntNumeral intNumeral = new IntNumeral(666);
    private static final LongNumeral longNumeral = new LongNumeral(9988776655L);
    private static final LongNumeral smallLongNumeral = new LongNumeral(838L);
    private static final LongNumeral negativeLongNumeral = new LongNumeral(-9988776655L);
    private static final BigIntNumeral bigIntNumeral = new BigIntNumeral("734273427342734273427342");
    private static final BigIntNumeral largeBigIntNumeral = new BigIntNumeral("5247833239524783323952478332395247833239");
    private static final BigIntNumeral hugeBigIntNumeral = new BigIntNumeral("5247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239524783323952478332395247833239");
    private static final BigIntNumeral smallBigIntNumeral = new BigIntNumeral(2473658642L);
    private static final BigIntNumeral negativeBigIntNumeral = new BigIntNumeral("-734273427342734273427342");
    private static final FloatNumeral floatNumeral = new FloatNumeral(3.3333e33f);
    private static final FloatNumeral smallFloatNumeral = new FloatNumeral(961.34f);
    private static final FloatNumeral negativeFloatNumeral = new FloatNumeral(-3.3333e33f);
    private static final DoubleNumeral doubleNumeral = new DoubleNumeral(5.2e77d);
    private static final DoubleNumeral smallDoubleNumeral = new DoubleNumeral(1.512153152e32d);
    private static final DoubleNumeral tinyDoubleNumeral = new DoubleNumeral(1.434543454e12d);
    private static final DoubleNumeral negativeDoubleNumeral = new DoubleNumeral(-5.2e77d);
    private static final BigDecNumeral bigDecNumeral = new BigDecNumeral("9.88765432102468e+420");
    private static final BigDecNumeral smallBigDecNumeral = new BigDecNumeral(1.111111e111d);
    private static final BigDecNumeral negativeBigDecNumeral = new BigDecNumeral("-9.88765432102468e+420");

    @Test
    public void toIntTest() {
        assertEquals(666, toInt(intNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(longNumeral));
        assertEquals(838, toInt(smallLongNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(negativeLongNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(bigIntNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(negativeBigIntNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(floatNumeral));
        assertEquals(961, toInt(smallFloatNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(negativeFloatNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(doubleNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(negativeDoubleNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(bigDecNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toInt(negativeBigDecNumeral));
    }

    @Test
    public void toLongTest() {
        assertEquals(666L, toLong(intNumeral));
        assertEquals(9988776655L, toLong(longNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(bigIntNumeral));
        assertEquals(2473658642L, toLong(smallBigIntNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(negativeBigIntNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(floatNumeral));
        assertEquals(961L, toLong(smallFloatNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(negativeFloatNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(doubleNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(negativeDoubleNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(bigDecNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toLong(negativeBigDecNumeral));
    }

    @Test
    public void toBigIntTest() {
        assertEquals(new BigInteger("666"), toBigInt(intNumeral));
        assertEquals(new BigInteger("9988776655"), toBigInt(longNumeral));
        assertEquals(new BigInteger("734273427342734273427342"), toBigInt(bigIntNumeral));
        assertEquals(new BigInteger("3333300000000000000000000000000000"), toBigInt(floatNumeral));
        assertEquals(new BigInteger("520000000000000000000000000000000000000000000000000000000000000000000000000000"), toBigInt(doubleNumeral));
        assertEquals(new BigInteger("9887654321024680000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), toBigInt(bigDecNumeral));
    }

    @Test
    public void toFloatTest() {
        assertEquals(666f, toFloat(intNumeral));
        assertEquals(9.988777e9f, toFloat(longNumeral));
        assertEquals(7.3427344e23f, toFloat(bigIntNumeral));
        assertEquals(3.3333e33f, toFloat(floatNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toFloat(doubleNumeral));
        assertEquals(1.5121531e32f, toFloat(smallDoubleNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toFloat(negativeDoubleNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toFloat(bigDecNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toFloat(negativeBigDecNumeral));
    }

    @Test
    public void toDoubleTest() {
        assertEquals(666d, toDouble(intNumeral));
        assertEquals(9.988776655e9d, toDouble(longNumeral));
        assertEquals(7.342734273427342e23d, toDouble(bigIntNumeral));
        assertEquals(3.3333e33d, toDouble(floatNumeral));
        assertEquals(5.2e77d, toDouble(doubleNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toDouble(bigDecNumeral));
        assertEquals(1.111111e111d, toDouble(smallBigDecNumeral));
        assertThrows(ExtendedArithmeticException.class, () -> toDouble(negativeBigDecNumeral));
    }

    @Test
    public void toBigDecTest() {
        assertEquals(new BigDecimal("666"), toBigDec(intNumeral));
        assertEquals(new BigDecimal("9988776655"), toBigDec(longNumeral));
        assertEquals(new BigDecimal("734273427342734273427342"), toBigDec(bigIntNumeral));
        assertEquals(new BigDecimal("3.3333e+33"), toBigDec(floatNumeral));
        assertEquals(new BigDecimal("5.2e+77"), toBigDec(doubleNumeral));
        assertEquals(new BigDecimal("9.88765432102468e+420"), toBigDec(bigDecNumeral));
    }

    @Test
    public void toSmallestTypeTest() {
        assertEquals(new IntNumeral(666), toSmallestType(intNumeral));
        assertEquals(new IntNumeral(838), toSmallestType(smallLongNumeral));
        assertEquals(new LongNumeral(2473658642L), toSmallestType(smallBigIntNumeral));
        assertEquals(new FloatNumeral(3.3333e33f), toSmallestType(floatNumeral));
        assertEquals(new FloatNumeral(1.5121531e32f), toSmallestType(smallDoubleNumeral));
        assertEquals(new DoubleNumeral(1.111111e111d), toSmallestType(smallBigDecNumeral));
    }

    @Test
    public void toIntegerTest() {
        assertEquals(new IntNumeral(961), toInteger(smallFloatNumeral));
        assertEquals(new LongNumeral(1434543454000L), toInteger(tinyDoubleNumeral));
        assertEquals(new BigIntNumeral("9887654321024680000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), toInteger(bigDecNumeral));
        assertSame(intNumeral, toInteger(intNumeral));
        assertSame(longNumeral, toInteger(longNumeral));
        assertSame(bigIntNumeral, toInteger(bigIntNumeral));
    }

    @Test
    public void toDecimalTest() {
        assertEquals(new FloatNumeral(666f), toDecimal(intNumeral));
        assertEquals(new FloatNumeral(9.988777e9f), toDecimal(longNumeral));
        assertEquals(new DoubleNumeral(5.2478332395247835e39), toDecimal(largeBigIntNumeral));
        assertEquals(new BigDecNumeral("5.247833239524783323952478332395248e+399"), toDecimal(hugeBigIntNumeral));
        assertSame(floatNumeral, toDecimal(floatNumeral));
        assertSame(doubleNumeral, toDecimal(doubleNumeral));
        assertSame(bigDecNumeral, toDecimal(bigDecNumeral));
    }

    @Test
    public void toNumeralTest() {
        assertEquals(new LongNumeral(666L), toNumeral(intNumeral, LONG));
        assertEquals(new BigDecNumeral("9988776655"), toNumeral(longNumeral, BIG_DEC));
        assertEquals(new FloatNumeral(7.3427344e23f), toNumeral(bigIntNumeral, FLOAT));
        assertEquals(new DoubleNumeral(3.3333e33d), toNumeral(floatNumeral, DOUBLE));
        assertEquals(new BigDecNumeral("5.2e+77"), toNumeral(doubleNumeral, BIG_DEC));
        assertEquals(new BigIntNumeral("9887654321024680000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), toNumeral(bigDecNumeral, BIG_INT));

        assertThrows(ExtendedArithmeticException.class, () -> toNumeral(longNumeral, INT));
        assertThrows(ExtendedArithmeticException.class, () -> toNumeral(bigIntNumeral, LONG));
        assertThrows(ExtendedArithmeticException.class, () -> toNumeral(floatNumeral, INT));
        assertThrows(ExtendedArithmeticException.class, () -> toNumeral(doubleNumeral, FLOAT));
        assertThrows(ExtendedArithmeticException.class, () -> toNumeral(bigDecNumeral, DOUBLE));
    }
}
