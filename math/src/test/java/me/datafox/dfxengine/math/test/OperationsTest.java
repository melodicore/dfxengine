package me.datafox.dfxengine.math.test;

import me.datafox.dfxengine.math.numeral.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static me.datafox.dfxengine.math.utils.Operations.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class OperationsTest {
    private static final IntNumeral zero = new IntNumeral(0);
    private static final IntNumeral one = new IntNumeral(1);
    private static final IntNumeral intNumeral = new IntNumeral(5);
    private static final IntNumeral largeIntNumeral = new IntNumeral(Integer.MAX_VALUE - 2);
    private static final IntNumeral negativeIntNumeral = new IntNumeral(Integer.MIN_VALUE + 2);
    private static final LongNumeral longNumeral = new LongNumeral(Long.MAX_VALUE - 2);
    private static final LongNumeral negativeLongNumeral = new LongNumeral(Long.MIN_VALUE + 2);
    private static final FloatNumeral floatNumeral = new FloatNumeral(4.57f);
    private static final FloatNumeral largeFloatNumeral = new FloatNumeral(Float.MAX_VALUE - Float.MAX_VALUE/10f);
    private static final FloatNumeral smallFloatNumeral = new FloatNumeral(0.01f);
    private static final FloatNumeral tinyFloatNumeral = new FloatNumeral(1e-10f);
    private static final FloatNumeral negativeFloatNumeral = new FloatNumeral(-Float.MAX_VALUE + Float.MAX_VALUE/10f);
    private static final DoubleNumeral doubleNumeral = new DoubleNumeral(Double.MAX_VALUE - Double.MAX_VALUE/10d);
    private static final DoubleNumeral negativeDoubleNumeral = new DoubleNumeral(-Double.MAX_VALUE + Double.MAX_VALUE/10d);
    private static final BigIntNumeral bigIntNumeral = new BigIntNumeral(BigInteger.valueOf(Long.MAX_VALUE).pow(5));
    private static final BigDecNumeral bigDecNumeral = new BigDecNumeral(BigDecimal.valueOf(Double.MAX_VALUE).pow(5));
    private static final BigDecNumeral otherBigDecNumeral = new BigDecNumeral(bigDecNumeral.bigDecValue().divide(BigDecimal.valueOf(2), MathContext.DECIMAL128));

    @Test
    public void addTest() {
        assertSame(floatNumeral, add(zero, floatNumeral));
        assertSame(floatNumeral, add(floatNumeral, zero));

        assertEquals(new IntNumeral(10), add(intNumeral, intNumeral));
        assertEquals(new LongNumeral(2147483650L), add(intNumeral, largeIntNumeral));
        assertEquals(new BigIntNumeral("9223372036854775810"), add(intNumeral, longNumeral));
        assertEquals(new BigIntNumeral("66749594872528440038659400431137192519314960677663778810642210898138927029830146832929608695812"), add(intNumeral, bigIntNumeral));

        assertEquals(new FloatNumeral(9.57f), add(intNumeral, floatNumeral));
        assertEquals(new DoubleNumeral(6.1250824423176155e38d), add(largeFloatNumeral, largeFloatNumeral));
        assertEquals(new BigDecNumeral("3.2358476427521684e+308"), add(doubleNumeral, doubleNumeral));
        assertEquals(new BigDecNumeral("2.816236083364434810182918633606957e+1541"), add(bigDecNumeral, otherBigDecNumeral));

        assertEquals(new LongNumeral(-4294967292L), add(negativeIntNumeral, negativeIntNumeral));
        assertEquals(new BigDecNumeral("-3.2358476427521684e+308"), add(negativeDoubleNumeral, negativeDoubleNumeral));
    }

    @Test
    public void subtractTest() {
        assertEquals(zero, subtract(intNumeral, intNumeral));
        assertEquals(zero, subtract(doubleNumeral, doubleNumeral));

        assertSame(floatNumeral, subtract(floatNumeral, zero));

        assertEquals(new IntNumeral(2147483640), subtract(largeIntNumeral, intNumeral));
        assertEquals(new FloatNumeral(0.42999983f), subtract(intNumeral, floatNumeral));

        assertEquals(new LongNumeral(-2147483651L), subtract(negativeIntNumeral, intNumeral));
        assertEquals(new BigIntNumeral("-18446744073709551611"), subtract(negativeLongNumeral, longNumeral));
        assertEquals(new BigIntNumeral("-66749594872528440038659400431137192519314960677663778810642210898138927029839370204966463471613"), subtract(negativeLongNumeral, bigIntNumeral));

        assertEquals(new DoubleNumeral(-6.1250824423176155e38d), subtract(negativeFloatNumeral, largeFloatNumeral));
        assertEquals(new BigDecNumeral("-3.2358476427521684e+308"), subtract(negativeDoubleNumeral, doubleNumeral));
        assertEquals(new BigDecNumeral("-9.387453611214782700609728778689856e+1540"), subtract(otherBigDecNumeral, bigDecNumeral));
    }

    @Test
    public void multiplyTest() {
        assertEquals(zero, multiply(zero, intNumeral));
        assertEquals(zero, multiply(floatNumeral, zero));

        assertSame(floatNumeral, multiply(one, floatNumeral));
        assertSame(intNumeral, multiply(intNumeral, one));

        assertEquals(new IntNumeral(25), multiply(intNumeral, intNumeral));
        assertEquals(new FloatNumeral(22.85f), multiply(intNumeral, floatNumeral));

        assertEquals(new LongNumeral(10737418225L), multiply(intNumeral, largeIntNumeral));
        assertEquals(new BigIntNumeral("46116860184273879025"), multiply(intNumeral, longNumeral));
        assertEquals(new BigIntNumeral("333747974362642200193297002155685962596574803388318894053211054490694635149150734164648043479035"), multiply(intNumeral, bigIntNumeral));

        assertEquals(new DoubleNumeral(1.3995813906415794e39d), multiply(floatNumeral, largeFloatNumeral));
        assertEquals(new BigDecNumeral("7.393911863688704794e+308"), multiply(floatNumeral, doubleNumeral));
        assertEquals(new BigDecNumeral("8.580132600650311388357292103722528e+1541"), multiply(floatNumeral, bigDecNumeral));

        assertEquals(new DoubleNumeral(-1.3995813906415794e39d), multiply(floatNumeral, negativeFloatNumeral));
        assertEquals(new BigDecNumeral("-7.393911863688704794e+308"), multiply(floatNumeral, negativeDoubleNumeral));
    }

    @Test
    public void divideTest() {
        assertThrows(ArithmeticException.class, () -> divide(intNumeral, zero));
        assertEquals(zero, divide(zero, intNumeral));
        assertEquals(one, divide(intNumeral, intNumeral));
        assertSame(intNumeral, divide(intNumeral, one));

        assertEquals(new IntNumeral(429496729), divide(largeIntNumeral, intNumeral));
        assertEquals(new FloatNumeral(0.91400003f), divide(floatNumeral, intNumeral));

        assertEquals(new DoubleNumeral(3.062541180269497e48d), divide(largeFloatNumeral, tinyFloatNumeral));
        assertEquals(new BigDecNumeral("1.6179238213760842e+318"), divide(doubleNumeral, tinyFloatNumeral));

        assertEquals(new DoubleNumeral(-3.062541180269497e48d), divide(negativeFloatNumeral, tinyFloatNumeral));
        assertEquals(new BigDecNumeral("-1.6179238213760842e+318"), divide(negativeDoubleNumeral, tinyFloatNumeral));

        assertEquals(new BigIntNumeral("7237005577332262212403911129196324050210709052721557111922331490884039737355"), divide(bigIntNumeral, longNumeral));
        assertEquals(new BigDecNumeral("1.160432090459057754195290624562501e+1233"), divide(bigDecNumeral, doubleNumeral));
    }

    @Test
    public void powerTest() {
        assertEquals(one, power(one, intNumeral));
        assertEquals(one, power(intNumeral, zero));
        assertEquals(one, power(zero, zero));
        assertEquals(zero, power(zero, intNumeral));
        assertSame(intNumeral, power(intNumeral, one));

        assertEquals(new IntNumeral(3125), power(intNumeral, intNumeral));
        assertEquals(new FloatNumeral(1564.2028f), power(intNumeral, floatNumeral));

        assertEquals(new LongNumeral(4611686005542486016L), power(largeIntNumeral, new IntNumeral(2)));
        assertEquals(new BigIntNumeral("45671925847575998096802168565788723718670778125"), power(largeIntNumeral, intNumeral));

        assertEquals(new DoubleNumeral(4.4352804649653303e42d), power(largeIntNumeral, floatNumeral));
        assertEquals(new BigDecNumeral("3.272942510499590855001576417190340e+1408"), power(doubleNumeral, floatNumeral));
    }

    @Test
    public void sqrtTest() {
        assertEquals(zero, sqrt(zero));
        assertEquals(one, sqrt(one));
        assertThrows(ArithmeticException.class, () -> sqrt(negativeIntNumeral));

        assertEquals(new IntNumeral(46340), sqrt(largeIntNumeral));
        assertEquals(new LongNumeral(3037000499L), sqrt(longNumeral));
        assertEquals(new BigIntNumeral("258359429617980926596565157283243452920303727631"), sqrt(bigIntNumeral));

        assertEquals(new FloatNumeral(1.7500117e19f), sqrt(largeFloatNumeral));
        assertEquals(new DoubleNumeral(1.2719763446605775e154d), sqrt(doubleNumeral));
        assertEquals(new BigDecNumeral("4.333002102749266541281042887375047e+770"), sqrt(bigDecNumeral));
    }

    @Test
    public void cbrtTest() {
        assertEquals(zero, cbrt(zero));
        assertEquals(one, cbrt(one));
        assertThrows(ArithmeticException.class, () -> cbrt(negativeIntNumeral));

        assertEquals(new IntNumeral(1290), cbrt(largeIntNumeral));
        assertEquals(new LongNumeral(2097152L), cbrt(longNumeral));
        assertEquals(new BigIntNumeral("40564819207303340840564425053525"), cbrt(bigIntNumeral));

        assertEquals(new FloatNumeral(6.7405291e12f), cbrt(largeFloatNumeral));
        assertEquals(new DoubleNumeral(5.449031976179548e102d), cbrt(doubleNumeral));
        assertEquals(new BigDecNumeral("5.726104460477540260293470052580462e+513"), cbrt(bigDecNumeral));
    }

    @Test
    public void rootTest() {
        assertThrows(ArithmeticException.class, () -> root(intNumeral, zero));
        assertEquals(zero, root(zero, intNumeral));
        assertEquals(one, root(one, intNumeral));
        assertSame(intNumeral, root(intNumeral, one));
        assertThrows(ArithmeticException.class, () -> root(negativeIntNumeral, intNumeral));

        assertEquals(new IntNumeral(73), root(largeIntNumeral, intNumeral));
        assertEquals(new LongNumeral(6208L), root(longNumeral, intNumeral));
        assertEquals(new BigIntNumeral("9223372036854775807"), root(bigIntNumeral, intNumeral));

        assertEquals(new FloatNumeral(2.63913632e8f), root(largeFloatNumeral, floatNumeral));
        assertEquals(new DoubleNumeral(2.7655730569490905e67d), root(doubleNumeral, floatNumeral));
        assertEquals(new BigDecNumeral("1.815467913392373723014819224859590e+337"), root(bigDecNumeral, floatNumeral));

        assertEquals(new DoubleNumeral(9.808963383654187e65d), root(floatNumeral, smallFloatNumeral));
        assertEquals(new BigDecNumeral("4.057703555403599985284286405572210e+3848"), root(largeFloatNumeral, smallFloatNumeral));
        assertEquals(new BigDecNumeral("2.279186996101751382324694711909063e+154127"), root(bigDecNumeral, smallFloatNumeral));
    }
}
