package me.datafox.dfxengine.math.test;

import me.datafox.dfxengine.math.numeral.*;
import me.datafox.dfxengine.math.utils.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    private static final BigIntNumeral bigIntWithinInt = new BigIntNumeral(BigInteger.valueOf(Integer.MAX_VALUE));
    private static final BigIntNumeral negativeBigIntWithinInt = new BigIntNumeral(BigInteger.valueOf(Integer.MIN_VALUE));
    private static final BigIntNumeral bigIntWithinLong = new BigIntNumeral(BigInteger.valueOf(Long.MAX_VALUE));
    private static final BigIntNumeral negativeBigIntWithinLong = new BigIntNumeral(BigInteger.valueOf(Long.MIN_VALUE));
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

    private static final BigDecNumeral bigDecWithinInt = new BigDecNumeral(BigDecimal.valueOf(Integer.MAX_VALUE));
    private static final BigDecNumeral negativeBigDecWithinInt = new BigDecNumeral(BigDecimal.valueOf(Integer.MIN_VALUE));
    private static final BigDecNumeral bigDecWithinLong = new BigDecNumeral(BigDecimal.valueOf(Long.MAX_VALUE));
    private static final BigDecNumeral negativeBigDecWithinLong = new BigDecNumeral(BigDecimal.valueOf(Long.MIN_VALUE));
    private static final BigDecNumeral bigDecWithinFloat = new BigDecNumeral(BigDecimal.valueOf(Float.MAX_VALUE));
    private static final BigDecNumeral negativeBigDecWithinFloat = new BigDecNumeral(BigDecimal.valueOf(-Float.MAX_VALUE));
    private static final BigDecNumeral bigDecWithinDouble = new BigDecNumeral(BigDecimal.valueOf(Double.MAX_VALUE));
    private static final BigDecNumeral negativeBigDecWithinDouble = new BigDecNumeral(BigDecimal.valueOf(-Double.MAX_VALUE));
    private static final BigDecNumeral bigDecOutsideDouble = new BigDecNumeral("1.7976931348623158e+308");
    private static final BigDecNumeral negativeBigDecOutsideDouble = new BigDecNumeral("-1.7976931348623158e+308");

    @Test
    public void isOutOfIntRangeTest() {
        Assertions.assertFalse(Range.isOutOfIntRange(intWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(negativeIntWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(longWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(negativeLongWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(bigIntWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(negativeBigIntWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(floatWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(negativeFloatWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(doubleWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(negativeDoubleWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(bigDecWithinInt));
        Assertions.assertFalse(Range.isOutOfIntRange(negativeBigDecWithinInt));

        Assertions.assertTrue(Range.isOutOfIntRange(longOutsideInt));
        Assertions.assertTrue(Range.isOutOfIntRange(negativeLongOutsideInt));
        Assertions.assertTrue(Range.isOutOfIntRange(bigIntWithinLong));
        Assertions.assertTrue(Range.isOutOfIntRange(negativeBigIntWithinLong));
        Assertions.assertTrue(Range.isOutOfIntRange(floatWithinLong));
        Assertions.assertTrue(Range.isOutOfIntRange(negativeFloatWithinLong));
        Assertions.assertTrue(Range.isOutOfIntRange(doubleWithinLong));
        Assertions.assertTrue(Range.isOutOfIntRange(negativeDoubleWithinLong));
        Assertions.assertTrue(Range.isOutOfIntRange(bigDecWithinLong));
        Assertions.assertTrue(Range.isOutOfIntRange(negativeBigDecWithinLong));
    }

    @Test
    public void isOutOfLongRangeTest() {
        Assertions.assertFalse(Range.isOutOfLongRange(intWithinInt));
        Assertions.assertFalse(Range.isOutOfLongRange(negativeIntWithinInt));
        Assertions.assertFalse(Range.isOutOfLongRange(longOutsideInt));
        Assertions.assertFalse(Range.isOutOfLongRange(negativeLongOutsideInt));
        Assertions.assertFalse(Range.isOutOfLongRange(bigIntWithinLong));
        Assertions.assertFalse(Range.isOutOfLongRange(negativeBigIntWithinLong));
        Assertions.assertFalse(Range.isOutOfLongRange(floatWithinLong));
        Assertions.assertFalse(Range.isOutOfLongRange(negativeFloatWithinLong));
        Assertions.assertFalse(Range.isOutOfLongRange(doubleWithinLong));
        Assertions.assertFalse(Range.isOutOfLongRange(negativeDoubleWithinLong));
        Assertions.assertFalse(Range.isOutOfLongRange(bigDecWithinLong));
        Assertions.assertFalse(Range.isOutOfLongRange(negativeBigDecWithinLong));

        Assertions.assertTrue(Range.isOutOfLongRange(bigIntWithinFloat));
        Assertions.assertTrue(Range.isOutOfLongRange(negativeBigIntWithinFloat));
        Assertions.assertTrue(Range.isOutOfLongRange(floatOutsideLong));
        Assertions.assertTrue(Range.isOutOfLongRange(negativeFloatOutsideLong));
        Assertions.assertTrue(Range.isOutOfLongRange(doubleWithinFloat));
        Assertions.assertTrue(Range.isOutOfLongRange(negativeDoubleWithinFloat));
        Assertions.assertTrue(Range.isOutOfLongRange(bigDecWithinFloat));
        Assertions.assertTrue(Range.isOutOfLongRange(negativeBigDecWithinFloat));
    }

    @Test
    public void isOutOfFloatRangeTest() {
        Assertions.assertFalse(Range.isOutOfFloatRange(intWithinInt));
        Assertions.assertFalse(Range.isOutOfFloatRange(negativeIntWithinInt));
        Assertions.assertFalse(Range.isOutOfFloatRange(longOutsideInt));
        Assertions.assertFalse(Range.isOutOfFloatRange(negativeLongOutsideInt));
        Assertions.assertFalse(Range.isOutOfFloatRange(bigIntWithinFloat));
        Assertions.assertFalse(Range.isOutOfFloatRange(negativeBigIntWithinFloat));
        Assertions.assertFalse(Range.isOutOfFloatRange(floatOutsideLong));
        Assertions.assertFalse(Range.isOutOfFloatRange(negativeFloatOutsideLong));
        Assertions.assertFalse(Range.isOutOfFloatRange(doubleWithinFloat));
        Assertions.assertFalse(Range.isOutOfFloatRange(negativeDoubleWithinFloat));
        Assertions.assertFalse(Range.isOutOfFloatRange(bigDecWithinFloat));
        Assertions.assertFalse(Range.isOutOfFloatRange(negativeBigDecWithinFloat));

        Assertions.assertTrue(Range.isOutOfFloatRange(bigIntWithinDouble));
        Assertions.assertTrue(Range.isOutOfFloatRange(negativeBigIntWithinDouble));
        Assertions.assertTrue(Range.isOutOfFloatRange(doubleOutsideFloat));
        Assertions.assertTrue(Range.isOutOfFloatRange(negativeDoubleOutsideFloat));
        Assertions.assertTrue(Range.isOutOfFloatRange(bigDecWithinDouble));
        Assertions.assertTrue(Range.isOutOfFloatRange(negativeBigDecWithinDouble));
    }

    @Test
    public void isOutOfDoubleRangeTest() {
        Assertions.assertFalse(Range.isOutOfDoubleRange(intWithinInt));
        Assertions.assertFalse(Range.isOutOfDoubleRange(negativeIntWithinInt));
        Assertions.assertFalse(Range.isOutOfDoubleRange(longOutsideInt));
        Assertions.assertFalse(Range.isOutOfDoubleRange(negativeLongOutsideInt));
        Assertions.assertFalse(Range.isOutOfDoubleRange(bigIntWithinDouble));
        Assertions.assertFalse(Range.isOutOfDoubleRange(negativeBigIntWithinDouble));
        Assertions.assertFalse(Range.isOutOfDoubleRange(floatOutsideLong));
        Assertions.assertFalse(Range.isOutOfDoubleRange(negativeFloatOutsideLong));
        Assertions.assertFalse(Range.isOutOfDoubleRange(doubleOutsideFloat));
        Assertions.assertFalse(Range.isOutOfDoubleRange(negativeDoubleOutsideFloat));
        Assertions.assertFalse(Range.isOutOfDoubleRange(bigDecWithinDouble));
        Assertions.assertFalse(Range.isOutOfDoubleRange(negativeBigDecWithinDouble));

        Assertions.assertTrue(Range.isOutOfDoubleRange(bigIntOutsideDouble));
        Assertions.assertTrue(Range.isOutOfDoubleRange(negativeBigIntOutsideDouble));
        Assertions.assertTrue(Range.isOutOfDoubleRange(bigDecOutsideDouble));
        Assertions.assertTrue(Range.isOutOfDoubleRange(negativeBigDecOutsideDouble));
    }
}
