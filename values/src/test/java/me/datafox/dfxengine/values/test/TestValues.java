package me.datafox.dfxengine.values.test;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.utils.Values;

import static me.datafox.dfxengine.values.test.TestHandles.*;

/**
 * @author datafox
 */
public class TestValues {
    public static Numeral intNumeral;
    public static Numeral longNumeral;
    public static Numeral bigIntNumeral;
    public static Numeral floatNumeral;
    public static Numeral doubleNumeral;
    public static Numeral bigDecNumeral;

    public static Value intValue;
    public static Value longValue;
    public static Value bigIntValue;
    public static Value floatValue;
    public static Value doubleValue;
    public static Value bigDecValue;

    public static Value immutableIntValue;
    public static Value immutableLongValue;
    public static Value immutableBigIntValue;
    public static Value immutableFloatValue;
    public static Value immutableDoubleValue;
    public static Value immutableBigDecValue;

    public static void initializeValues() {
        intNumeral = Numerals.of(99456);
        longNumeral = Numerals.of(47567929325878132L);
        bigIntNumeral = Numerals.of("28975389235899203095285238");
        floatNumeral = Numerals.of(1.6624e30f);
        doubleNumeral = Numerals.of(5.1234514e142d);
        bigDecNumeral = Numerals.of("2.68942368927827453664274e+555");

        intValue = Values.mutable(intHandle, intNumeral);
        longValue = Values.mutable(longHandle, longNumeral);
        bigIntValue = Values.mutable(bigIntHandle, bigIntNumeral);
        floatValue = Values.mutable(floatHandle, floatNumeral);
        doubleValue = Values.mutable(doubleHandle, doubleNumeral);
        bigDecValue = Values.mutable(bigDecHandle, bigDecNumeral);

        immutableIntValue = Values.immutable(intHandle, intNumeral);
        immutableLongValue = Values.immutable(longHandle, longNumeral);
        immutableBigIntValue = Values.immutable(bigIntHandle, bigIntNumeral);
        immutableFloatValue = Values.immutable(floatHandle, floatNumeral);
        immutableDoubleValue = Values.immutable(doubleHandle, doubleNumeral);
        immutableBigDecValue = Values.immutable(bigDecHandle, bigDecNumeral);
    }
}
