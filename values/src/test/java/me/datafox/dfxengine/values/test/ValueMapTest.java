package me.datafox.dfxengine.values.test;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.DelegatedValueMap;
import me.datafox.dfxengine.values.api.ValueMap;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.operation.MapMathContext;
import me.datafox.dfxengine.values.api.operation.MathContext;
import me.datafox.dfxengine.values.utils.Modifiers;
import me.datafox.dfxengine.values.utils.Values;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static me.datafox.dfxengine.math.api.NumeralType.*;
import static me.datafox.dfxengine.values.test.TestHandles.*;
import static me.datafox.dfxengine.values.test.TestValues.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class ValueMapTest {
    public static ValueMap valueMap;

    @BeforeEach
    public void beforeEach() {
        initializeHandles();
        initializeValues();

        valueMap = DelegatedValueMap
                .builder(space)
                .value(intValue)
                .value(bigIntValue)
                .value(doubleValue)
                .value(bigDecValue)
                .modifier(Modifiers.add(0, Values.of(1)))
                .build();
    }

    @Test
    public void convertTest() {
        assertThrows(ExtendedArithmeticException.class, () -> valueMap.convert(LONG));
        assertEquals(INT, valueMap.get(intHandle).getValue().getType());
        valueMap.convert(BIG_DEC);
        assertEquals(BIG_DEC, valueMap.get(doubleHandle).getValue().getType());
        assertThrows(ExtendedArithmeticException.class, () -> valueMap.convert(Set.of(doubleHandle), FLOAT));
        valueMap.convert(Set.of(intHandle), FLOAT);
        assertEquals(FLOAT, valueMap.get(intHandle).getValue().getType());
        assertThrows(ExtendedArithmeticException.class, () -> valueMap.convert(Map.of(doubleHandle, FLOAT)));
        valueMap.convert(Map.of(doubleHandle, DOUBLE));
        assertEquals(DOUBLE, valueMap.get(doubleHandle).getValue().getType());
    }

    @Test
    public void convertAllowedTest() {
        valueMap.convertAllowed(FLOAT);
        assertEquals(FLOAT, valueMap.get(intHandle).getValue().getType());
        assertEquals(FLOAT, valueMap.get(bigIntHandle).getValue().getType());
        assertEquals(DOUBLE, valueMap.get(doubleHandle).getValue().getType());
        assertEquals(BIG_DEC, valueMap.get(bigDecHandle).getValue().getType());
    }

    @Test
    public void toIntegerTest() {
        valueMap.toInteger();
        assertEquals(INT, valueMap.get(intHandle).getValue().getType());
        assertEquals(BIG_INT, valueMap.get(bigIntHandle).getValue().getType());
        assertEquals(BIG_INT, valueMap.get(doubleHandle).getValue().getType());
        assertEquals(BIG_INT, valueMap.get(bigDecHandle).getValue().getType());
    }

    @Test
    public void toDecimalTest() {
        valueMap.toDecimal();
        assertEquals(FLOAT, valueMap.get(intHandle).getValue().getType());
        assertEquals(FLOAT, valueMap.get(bigIntHandle).getValue().getType());
        assertEquals(DOUBLE, valueMap.get(doubleHandle).getValue().getType());
        assertEquals(BIG_DEC, valueMap.get(bigDecHandle).getValue().getType());
    }

    @Test
    public void toSmallestTypeTest() {
        valueMap.putHandled(Values.of(longHandle, 1000L));
        valueMap.toSmallestType();
        assertEquals(INT, valueMap.get(intHandle).getValue().getType());
        assertEquals(INT, valueMap.get(longHandle).getValue().getType());
        valueMap.putHandled(Values.of(longHandle, 1000L));
        valueMap.toSmallestType(Set.of(intHandle));
        assertEquals(INT, valueMap.get(intHandle).getValue().getType());
        assertEquals(LONG, valueMap.get(longHandle).getValue().getType());
    }

    @Test
    public void setTest() {
        valueMap.set(Numerals.of(420L));
        assertEquals(Numerals.of(421L), valueMap.get(doubleHandle).getValue());
        valueMap.set(Set.of(intHandle, longHandle), Numerals.of(5.23e13d));
        assertEquals(Numerals.of(5.2300000000001e13d), valueMap.get(intHandle).getValue());
        assertNull(valueMap.get(longHandle));
        valueMap.set(MapMathContext.builder().createNonExistingAs(Numerals.of(0)).build(), Set.of(longHandle), Numerals.of(4056181923489831L));
        assertEquals(Numerals.of(4056181923489832L), valueMap.get(longHandle).getValue());
        valueMap.set(Map.of(doubleHandle, Numerals.of(52)));
        assertEquals(Numerals.of(53), valueMap.get(doubleHandle).getValue());
    }

    @Test
    public void applyTest() {
        valueMap.apply(Operations::add, Numerals.of(3912));
        assertEquals(Numerals.of(103369), valueMap.get(intHandle).getValue());
        assertEquals(Numerals.of("28975389235899203095289151"), valueMap.get(bigIntHandle).getValue());
        assertEquals(Numerals.of(5.1234514e142d), valueMap.get(doubleHandle).getValue());
        assertEquals(Numerals.of("2.689423689278274536642740000000000e555"), valueMap.get(bigDecHandle).getValue());

        assertThrows(ExtendedArithmeticException.class, () -> valueMap.apply(Operations::multiply, MathContext.builder().convertResultTo(FLOAT).ignoreBadConversion(false).build(), Numerals.of(1.2f)));

        assertEquals(Numerals.of(124042.6f), valueMap.get(intHandle).getValue());
        assertEquals(Numerals.of(3.4770466e25f), valueMap.get(bigIntHandle).getValue());
        assertEquals(Numerals.of(5.1234514e142d), valueMap.get(doubleHandle).getValue());
        assertEquals(Numerals.of("2.689423689278274536642740000000000e555"), valueMap.get(bigDecHandle).getValue());
    }

    @Test
    public void compareTest() {
        assertTrue(valueMap.compare(Comparison.greaterThan(), Numerals.of(0)));
        assertFalse(valueMap.compare(Comparison.greaterThan(), Numerals.of(1000000)));
        assertTrue(valueMap.compare(Comparison.greaterThan(), Set.of(bigDecHandle, doubleHandle), Numerals.of(1000000)));
        assertFalse(valueMap.compare(Comparison.greaterThan(), Map.of(doubleHandle, Numerals.of(5.1234514e143d))));
        assertTrue(valueMap.compare(Comparison.greaterThan(), Map.of(bigDecHandle, Numerals.of(5.1234514e143d))));
    }

    @Test
    public void numeralMapTest() {
        Map<Handle, Numeral> map = valueMap.getBaseNumeralMap();
        assertEquals(intNumeral, map.get(intHandle));
        map = valueMap.getValueNumeralMap();
        assertEquals(Numerals.of(99457), map.get(intHandle));
        assertNull(map.get(longHandle));
        valueMap.putHandled(longValue);
        assertEquals(Numerals.of(47567929325878133L), map.get(longHandle));
    }
}
