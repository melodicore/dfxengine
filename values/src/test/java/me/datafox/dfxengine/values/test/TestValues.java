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

    public static void initializeValues() {
        intNumeral = Numerals.of(99456);
        longNumeral = Numerals.of(47567929325878132L);
        bigIntNumeral = Numerals.of("28975389235899203095285238");
        floatNumeral = Numerals.of(1.6624e30f);
        doubleNumeral = Numerals.of(5.1234514e142d);
        bigDecNumeral = Numerals.of("2.68942368927827453664274e+555");

        intValue = Values.of(intHandle, intNumeral);
        longValue = Values.of(longHandle, longNumeral);
        bigIntValue = Values.of(bigIntHandle, bigIntNumeral);
        floatValue = Values.of(floatHandle, floatNumeral);
        doubleValue = Values.of(doubleHandle, doubleNumeral);
        bigDecValue = Values.of(bigDecHandle, bigDecNumeral);
    }
}
