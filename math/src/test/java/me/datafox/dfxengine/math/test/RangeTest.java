package me.datafox.dfxengine.math.test;

import me.datafox.dfxengine.math.numeral.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static me.datafox.dfxengine.math.utils.Range.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author datafox
 */
public class RangeTest {
    private static final IntNumeral intWithinInt = new IntNumeral(Integer.MAX_VALUE);
    private static final IntNumeral negativeIntWithinInt = new IntNumeral(Integer.MIN_VALUE);

    private static final LongNumeral longWithinInt = new LongNumeral(Integer.MAX_VALUE);
    private static final LongNumeral negativeLongWithinInt = new LongNumeral(Integer.MIN_VALUE);
    private static final LongNumeral longOutsideInt = new LongNumeral(Integer.MAX_VALUE + 1L);
    private static final LongNumeral negativeLongOutsideInt = new LongNumeral(Integer.MIN_VALUE - 1L);

    private static final BigIntNumeral bigIntWithinInt = new BigIntNumeral(Integer.MAX_VALUE);
    private static final BigIntNumeral negativeBigIntWithinInt = new BigIntNumeral(Integer.MIN_VALUE);
    private static final BigIntNumeral bigIntWithinLong = new BigIntNumeral(Long.MAX_VALUE);
    private static final BigIntNumeral negativeBigIntWithinLong = new BigIntNumeral(Long.MIN_VALUE);
    private static final BigIntNumeral bigIntWithinFloat = new BigIntNumeral(BigDecimal.valueOf(Float.MAX_VALUE).toBigInteger());
    private static final BigIntNumeral negativeBigIntWithinFloat = new BigIntNumeral(BigDecimal.valueOf(-Float.MAX_VALUE).toBigInteger());
    private static final BigIntNumeral bigIntWithinDouble = new BigIntNumeral(BigDecimal.valueOf(Double.MAX_VALUE).toBigInteger());
    private static final BigIntNumeral negativeBigIntWithinDouble = new BigIntNumeral(BigDecimal.valueOf(-Double.MAX_VALUE).toBigInteger());
    private static final BigIntNumeral bigIntOutsideDouble = new BigIntNumeral(BigDecimal.valueOf(Double.MAX_VALUE).toBigInteger().add(BigInteger.ONE));
    private static final BigIntNumeral negativeBigIntOutsideDouble = new BigIntNumeral(BigDecimal.valueOf(-Double.MAX_VALUE).toBigInteger().subtract(BigInteger.ONE));

    private static final FloatNumeral floatWithinInt = new FloatNumeral(Integer.MAX_VALUE);
    private static final FloatNumeral negativeFloatWithinInt = new FloatNumeral(Integer.MIN_VALUE);
    private static final FloatNumeral floatWithinLong = new FloatNumeral(Long.MAX_VALUE);
    private static final FloatNumeral negativeFloatWithinLong = new FloatNumeral(Long.MIN_VALUE);
    private static final FloatNumeral floatOutsideLong = new FloatNumeral(Long.MAX_VALUE + 1.09951163e12f);
    private static final FloatNumeral negativeFloatOutsideLong = new FloatNumeral(Long.MIN_VALUE - 1.09951163e12f);

    private static final DoubleNumeral doubleWithinInt = new DoubleNumeral(Integer.MAX_VALUE);
    private static final DoubleNumeral negativeDoubleWithinInt = new DoubleNumeral(Integer.MIN_VALUE);
    private static final DoubleNumeral doubleWithinLong = new DoubleNumeral(Long.MAX_VALUE);
    private static final DoubleNumeral negativeDoubleWithinLong = new DoubleNumeral(Long.MIN_VALUE);
    private static final DoubleNumeral doubleWithinFloat = new DoubleNumeral(Float.MAX_VALUE);
    private static final DoubleNumeral negativeDoubleWithinFloat = new DoubleNumeral(-Float.MAX_VALUE);
    private static final DoubleNumeral doubleOutsideFloat = new DoubleNumeral(Float.MAX_VALUE + 3.777893186295716e22d);
    private static final DoubleNumeral negativeDoubleOutsideFloat = new DoubleNumeral(-Float.MAX_VALUE - 3.777893186295716e22d);

    private static final BigDecNumeral bigDecWithinInt = new BigDecNumeral(Integer.MAX_VALUE);
    private static final BigDecNumeral negativeBigDecWithinInt = new BigDecNumeral(Integer.MIN_VALUE);
    private static final BigDecNumeral bigDecWithinLong = new BigDecNumeral(Long.MAX_VALUE);
    private static final BigDecNumeral negativeBigDecWithinLong = new BigDecNumeral(Long.MIN_VALUE);
    private static final BigDecNumeral bigDecWithinFloat = new BigDecNumeral(Float.MAX_VALUE);
    private static final BigDecNumeral negativeBigDecWithinFloat = new BigDecNumeral(-Float.MAX_VALUE);
    private static final BigDecNumeral bigDecWithinDouble = new BigDecNumeral(Double.MAX_VALUE);
    private static final BigDecNumeral negativeBigDecWithinDouble = new BigDecNumeral(-Double.MAX_VALUE);
    private static final BigDecNumeral bigDecOutsideDouble = new BigDecNumeral("1.7976931348623158e+308");
    private static final BigDecNumeral negativeBigDecOutsideDouble = new BigDecNumeral("-1.7976931348623158e+308");

    @Test
    public void isOutOfIntRangeTest() {
        assertFalse(isOutOfIntRange(intWithinInt));
        assertFalse(isOutOfIntRange(negativeIntWithinInt));
        assertFalse(isOutOfIntRange(longWithinInt));
        assertFalse(isOutOfIntRange(negativeLongWithinInt));
        assertFalse(isOutOfIntRange(bigIntWithinInt));
        assertFalse(isOutOfIntRange(negativeBigIntWithinInt));
        assertFalse(isOutOfIntRange(floatWithinInt));
        assertFalse(isOutOfIntRange(negativeFloatWithinInt));
        assertFalse(isOutOfIntRange(doubleWithinInt));
        assertFalse(isOutOfIntRange(negativeDoubleWithinInt));
        assertFalse(isOutOfIntRange(bigDecWithinInt));
        assertFalse(isOutOfIntRange(negativeBigDecWithinInt));

        assertTrue(isOutOfIntRange(longOutsideInt));
        assertTrue(isOutOfIntRange(negativeLongOutsideInt));
        assertTrue(isOutOfIntRange(bigIntWithinLong));
        assertTrue(isOutOfIntRange(negativeBigIntWithinLong));
        assertTrue(isOutOfIntRange(floatWithinLong));
        assertTrue(isOutOfIntRange(negativeFloatWithinLong));
        assertTrue(isOutOfIntRange(doubleWithinLong));
        assertTrue(isOutOfIntRange(negativeDoubleWithinLong));
        assertTrue(isOutOfIntRange(bigDecWithinLong));
        assertTrue(isOutOfIntRange(negativeBigDecWithinLong));
    }

    @Test
    public void isOutOfLongRangeTest() {
        assertFalse(isOutOfLongRange(intWithinInt));
        assertFalse(isOutOfLongRange(negativeIntWithinInt));
        assertFalse(isOutOfLongRange(longOutsideInt));
        assertFalse(isOutOfLongRange(negativeLongOutsideInt));
        assertFalse(isOutOfLongRange(bigIntWithinLong));
        assertFalse(isOutOfLongRange(negativeBigIntWithinLong));
        assertFalse(isOutOfLongRange(floatWithinLong));
        assertFalse(isOutOfLongRange(negativeFloatWithinLong));
        assertFalse(isOutOfLongRange(doubleWithinLong));
        assertFalse(isOutOfLongRange(negativeDoubleWithinLong));
        assertFalse(isOutOfLongRange(bigDecWithinLong));
        assertFalse(isOutOfLongRange(negativeBigDecWithinLong));

        assertTrue(isOutOfLongRange(bigIntWithinFloat));
        assertTrue(isOutOfLongRange(negativeBigIntWithinFloat));
        assertTrue(isOutOfLongRange(floatOutsideLong));
        assertTrue(isOutOfLongRange(negativeFloatOutsideLong));
        assertTrue(isOutOfLongRange(doubleWithinFloat));
        assertTrue(isOutOfLongRange(negativeDoubleWithinFloat));
        assertTrue(isOutOfLongRange(bigDecWithinFloat));
        assertTrue(isOutOfLongRange(negativeBigDecWithinFloat));
    }

    @Test
    public void isOutOfFloatRangeTest() {
        assertFalse(isOutOfFloatRange(intWithinInt));
        assertFalse(isOutOfFloatRange(negativeIntWithinInt));
        assertFalse(isOutOfFloatRange(longOutsideInt));
        assertFalse(isOutOfFloatRange(negativeLongOutsideInt));
        assertFalse(isOutOfFloatRange(bigIntWithinFloat));
        assertFalse(isOutOfFloatRange(negativeBigIntWithinFloat));
        assertFalse(isOutOfFloatRange(floatOutsideLong));
        assertFalse(isOutOfFloatRange(negativeFloatOutsideLong));
        assertFalse(isOutOfFloatRange(doubleWithinFloat));
        assertFalse(isOutOfFloatRange(negativeDoubleWithinFloat));
        assertFalse(isOutOfFloatRange(bigDecWithinFloat));
        assertFalse(isOutOfFloatRange(negativeBigDecWithinFloat));

        assertTrue(isOutOfFloatRange(bigIntWithinDouble));
        assertTrue(isOutOfFloatRange(negativeBigIntWithinDouble));
        assertTrue(isOutOfFloatRange(doubleOutsideFloat));
        assertTrue(isOutOfFloatRange(negativeDoubleOutsideFloat));
        assertTrue(isOutOfFloatRange(bigDecWithinDouble));
        assertTrue(isOutOfFloatRange(negativeBigDecWithinDouble));
    }

    @Test
    public void isOutOfDoubleRangeTest() {
        assertFalse(isOutOfDoubleRange(intWithinInt));
        assertFalse(isOutOfDoubleRange(negativeIntWithinInt));
        assertFalse(isOutOfDoubleRange(longOutsideInt));
        assertFalse(isOutOfDoubleRange(negativeLongOutsideInt));
        assertFalse(isOutOfDoubleRange(bigIntWithinDouble));
        assertFalse(isOutOfDoubleRange(negativeBigIntWithinDouble));
        assertFalse(isOutOfDoubleRange(floatOutsideLong));
        assertFalse(isOutOfDoubleRange(negativeFloatOutsideLong));
        assertFalse(isOutOfDoubleRange(doubleOutsideFloat));
        assertFalse(isOutOfDoubleRange(negativeDoubleOutsideFloat));
        assertFalse(isOutOfDoubleRange(bigDecWithinDouble));
        assertFalse(isOutOfDoubleRange(negativeBigDecWithinDouble));

        assertTrue(isOutOfDoubleRange(bigIntOutsideDouble));
        assertTrue(isOutOfDoubleRange(negativeBigIntOutsideDouble));
        assertTrue(isOutOfDoubleRange(bigDecOutsideDouble));
        assertTrue(isOutOfDoubleRange(negativeBigDecOutsideDouble));
    }
}
