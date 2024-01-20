package me.datafox.dfxengine.values.test;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.Modifier;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.comparison.Comparison;
import me.datafox.dfxengine.values.api.operation.MathContext;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.utils.Modifiers;
import me.datafox.dfxengine.values.utils.Values;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static me.datafox.dfxengine.math.api.NumeralType.*;
import static me.datafox.dfxengine.values.test.TestHandles.initializeHandles;
import static me.datafox.dfxengine.values.test.TestValues.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class ValueTest {
    @BeforeEach
    public void beforeEach() {
        initializeHandles();
        initializeValues();
    }

    @Test
    public void canConvertTest() {
        assertTrue(intValue.canConvert(INT));
        assertTrue(intValue.canConvert(LONG));
        assertTrue(intValue.canConvert(BIG_INT));
        assertTrue(intValue.canConvert(FLOAT));
        assertTrue(intValue.canConvert(DOUBLE));
        assertTrue(intValue.canConvert(BIG_DEC));

        assertFalse(longValue.canConvert(INT));
        assertTrue(longValue.canConvert(LONG));
        assertTrue(longValue.canConvert(BIG_INT));
        assertTrue(longValue.canConvert(FLOAT));
        assertTrue(longValue.canConvert(DOUBLE));
        assertTrue(longValue.canConvert(BIG_DEC));

        assertFalse(bigIntValue.canConvert(INT));
        assertFalse(bigIntValue.canConvert(LONG));
        assertTrue(bigIntValue.canConvert(BIG_INT));
        assertTrue(bigIntValue.canConvert(FLOAT));
        assertTrue(bigIntValue.canConvert(DOUBLE));
        assertTrue(bigIntValue.canConvert(BIG_DEC));

        assertFalse(floatValue.canConvert(INT));
        assertFalse(floatValue.canConvert(LONG));
        assertTrue(floatValue.canConvert(BIG_INT));
        assertTrue(floatValue.canConvert(FLOAT));
        assertTrue(floatValue.canConvert(DOUBLE));
        assertTrue(floatValue.canConvert(BIG_DEC));

        assertFalse(doubleValue.canConvert(INT));
        assertFalse(doubleValue.canConvert(LONG));
        assertTrue(doubleValue.canConvert(BIG_INT));
        assertFalse(doubleValue.canConvert(FLOAT));
        assertTrue(doubleValue.canConvert(DOUBLE));
        assertTrue(doubleValue.canConvert(BIG_DEC));

        assertFalse(bigDecValue.canConvert(INT));
        assertFalse(bigDecValue.canConvert(LONG));
        assertTrue(bigDecValue.canConvert(BIG_INT));
        assertFalse(bigDecValue.canConvert(FLOAT));
        assertFalse(bigDecValue.canConvert(DOUBLE));
        assertTrue(bigDecValue.canConvert(BIG_DEC));
    }

    @Test
    public void convertTest() {
        assertTrue(intValue.convert(LONG));
        assertEquals(Numerals.of(99456L), intValue.getBase());
        assertTrue(intValue.convert(BIG_INT));
        assertEquals(Numerals.of("99456"), intValue.getBase());
        assertTrue(intValue.convert(FLOAT));
        assertEquals(Numerals.of(99456f), intValue.getBase());
        assertTrue(intValue.convert(DOUBLE));
        assertEquals(Numerals.of(99456d), intValue.getBase());
        assertTrue(intValue.convert(BIG_DEC));
        assertEquals(Numerals.of("99456.0"), intValue.getBase());
        assertTrue(intValue.convert(INT));
        assertEquals(intNumeral, intValue.getBase());

        assertThrows(ExtendedArithmeticException.class, () -> bigDecValue.convert(INT));
        assertEquals(bigDecNumeral, bigDecValue.getBase());
        assertThrows(ExtendedArithmeticException.class, () -> bigDecValue.convert(LONG));
        assertEquals(bigDecNumeral, bigDecValue.getBase());
        assertThrows(ExtendedArithmeticException.class, () -> bigDecValue.convert(FLOAT));
        assertEquals(bigDecNumeral, bigDecValue.getBase());
        assertThrows(ExtendedArithmeticException.class, () -> bigDecValue.convert(DOUBLE));
        assertEquals(bigDecNumeral, bigDecValue.getBase());
    }

    @Test
    public void convertIfAllowedTest() {
        assertFalse(floatValue.convertIfAllowed(INT));
        assertEquals(floatNumeral, floatValue.getBase());
        assertFalse(floatValue.convertIfAllowed(LONG));
        assertEquals(floatNumeral, floatValue.getBase());
        assertFalse(floatValue.convertIfAllowed(FLOAT));
        assertEquals(floatNumeral, floatValue.getBase());
        assertTrue(floatValue.convertIfAllowed(DOUBLE));
        assertEquals(Numerals.of(1.6624e30d), floatValue.getBase());
        assertTrue(floatValue.convertIfAllowed(BIG_INT));
        assertEquals(Numerals.of("1662400000000000000000000000000"), floatValue.getBase());
        assertTrue(floatValue.convertIfAllowed(BIG_DEC));
        assertEquals(Numerals.of("1.6624e+30"), floatValue.getBase());
        assertTrue(floatValue.convertIfAllowed(FLOAT));
        assertEquals(floatNumeral, floatValue.getBase());
    }

    public void toIntegerTest() {
        assertFalse(intValue.toInteger());
        assertEquals(intNumeral, intValue.getBase());
        assertFalse(longValue.toInteger());
        assertEquals(longNumeral, longValue.getBase());
        assertFalse(bigIntValue.toInteger());
        assertEquals(bigIntNumeral, bigIntValue.getBase());
        assertTrue(floatValue.toInteger());
        assertEquals(Numerals.of("1662400000000000000000000000000"), floatValue.getBase());
        assertTrue(doubleValue.toInteger());
        assertEquals(Numerals.of("51234514000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), doubleValue.getBase());
        assertTrue(bigDecValue.toInteger());
        assertEquals(Numerals.of("2689423689278274536642740000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), bigDecValue.getBase());
    }

    public void toDecimalTest() {
        assertTrue(intValue.toDecimal());
        assertEquals(Numerals.of(99456.0f), intValue.getBase());
        assertTrue(longValue.toDecimal());
        assertEquals(Numerals.of(4.7567931e16f), longValue.getBase());
        assertTrue(bigIntValue.toDecimal());
        assertEquals(Numerals.of(2.897539e25f), bigIntValue.getBase());
        assertFalse(floatValue.toDecimal());
        assertEquals(floatNumeral, floatValue.getBase());
        assertFalse(doubleValue.toDecimal());
        assertEquals(doubleNumeral, doubleValue.getBase());
        assertFalse(bigDecValue.toDecimal());
        assertEquals(bigDecNumeral, bigDecValue.getBase());
    }

    @Test
    public void toSmallestTypeTest() {
        assertTrue(intValue.convert(BIG_INT));
        assertEquals(Numerals.of("99456"), intValue.getBase());
        assertTrue(longValue.convert(BIG_INT));
        assertEquals(Numerals.of("47567929325878132"), longValue.getBase());
        assertTrue(floatValue.convert(BIG_DEC));
        assertEquals(Numerals.of("1.6624e+30"), floatValue.getBase());
        assertTrue(doubleValue.convert(BIG_DEC));
        assertEquals(Numerals.of("5.1234514e+142"), doubleValue.getBase());

        assertTrue(intValue.toSmallestType());
        assertEquals(intNumeral, intValue.getBase());
        assertTrue(longValue.toSmallestType());
        assertEquals(longNumeral, longValue.getBase());
        assertTrue(floatValue.toSmallestType());
        assertEquals(floatNumeral, floatValue.getBase());
        assertTrue(doubleValue.toSmallestType());
        assertEquals(doubleNumeral, doubleValue.getBase());
    }

    @Test
    public void setTest() {
        intValue.set(Numerals.of(3742L));
        assertEquals(Numerals.of(3742L), intValue.getBase());
        intValue.set(Numerals.of(3.674753e27d));
        assertEquals(Numerals.of(3.674753e27d), intValue.getBase());
    }

    @Test
    public void applyTest() {
        intValue.apply(new ToDoubleTestOperation());
        assertEquals(Numerals.of(99456.0d), intValue.getBase());
        intValue.apply(Operations::sqrt, MathContext.builder().convertResultTo(FLOAT).build());
        assertEquals(Numerals.of(315.36646f), intValue.getBase());
        intValue.apply(Operations::power, Numerals.of("1.5"));
        assertEquals(Numerals.of("5600.454261623189947408056716691534"), intValue.getBase());
        intValue.apply(Operations::lerp, MathContext.builder().convertResultTo(LONG).build(), Numerals.of(-1), Numerals.of(Math.E));
        assertEquals(Numerals.of(20823L), intValue.getBase());
    }

    @Test
    public void compareTest() {
        assertTrue(intValue.compare(Comparison.greaterThan(), Numerals.of(20)));
        assertTrue(longValue.compare(Comparison.equal(), Numerals.of(47567929325878132.0)));
        assertFalse(longValue.compare(Comparison.strictEqual(), Numerals.of(47567929325878132.0)));
    }

    @Test
    public void modifierTest() {
        Modifier add = Modifiers.add(0, longValue);
        assertTrue(intValue.addModifier(add));
        assertFalse(intValue.addModifier(add));
        assertEquals(Numerals.of(99456), intValue.getBase());
        assertEquals(Numerals.of(47567929325977588L), intValue.getValue());
        intValue.set(Numerals.of(40));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(47567929325878172L), intValue.getValue());
        longValue.set(Numerals.of(40));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(80), intValue.getValue());
        Modifier sqrt = Modifiers.sqrt(10);
        assertTrue(intValue.addModifier(sqrt));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(8), intValue.getValue());
        longValue.set(Numerals.of(40.0));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(8.94427190999916d), intValue.getValue());
        assertTrue(longValue.addModifier(Modifiers.power(0, Values.of(Math.PI))));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(328.54165043166324d), intValue.getValue());
        assertTrue(intValue.addModifier(Modifiers.operation(20, Numeral::toInteger)));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(328), intValue.getValue());
        assertTrue(intValue.containsModifier(add));
        assertTrue(intValue.containsModifiers(Set.of(add, sqrt)));
        assertTrue(intValue.removeModifier(add));
        assertFalse(intValue.removeModifier(add));
        assertFalse(intValue.containsModifier(add));
        assertFalse(intValue.containsModifiers(Set.of(add, sqrt)));
        assertTrue(intValue.containsModifier(sqrt));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(6), intValue.getValue());
        assertTrue(intValue.addModifiers(Set.of(add, sqrt)));
        assertFalse(intValue.addModifiers(Set.of(add, sqrt)));
        assertTrue(intValue.removeModifiers(Set.of(add, sqrt)));
        assertFalse(intValue.removeModifiers(Set.of(add, sqrt)));
        assertEquals(Numerals.of(40), intValue.getBase());
        assertEquals(Numerals.of(40), intValue.getValue());
    }

    @Test
    public void staticValueTest() {
        Value staticValue = Values.of(9000.0001d);

        assertNull(staticValue.getHandle());
        assertEquals(Numerals.of(9000.0001d), staticValue.getBase());
        assertEquals(Numerals.of(9000.0001d), staticValue.getValue());
        assertTrue(staticValue.isStatic());
        assertFalse(staticValue.canConvert(INT));
        assertTrue(staticValue.canConvert(DOUBLE));
        assertFalse(staticValue.convert(DOUBLE));
        assertThrows(UnsupportedOperationException.class, () -> staticValue.convert(INT));
        assertFalse(staticValue.convertIfAllowed(INT));
        assertFalse(staticValue.convertIfAllowed(DOUBLE));
        assertThrows(UnsupportedOperationException.class, staticValue::toInteger);
        assertFalse(staticValue.toDecimal());
        assertFalse(staticValue.toSmallestType());
        assertThrows(UnsupportedOperationException.class, () -> staticValue.set(Numerals.of(345L)));
        assertThrows(UnsupportedOperationException.class, () -> staticValue.apply(Operations::log));
        assertThrows(UnsupportedOperationException.class, () -> staticValue.apply(Operations::add, Numerals.of(7)));
        assertThrows(UnsupportedOperationException.class, () -> staticValue.apply(Operations::lerp, Numerals.of(7), Numerals.of(9)));
        assertTrue(staticValue.compare(Comparison.greaterThan(), Numerals.of(9000.0d)));
        assertEquals(Set.of(), staticValue.getModifiers());
        Modifier sqrt = Modifiers.sqrt(0);
        Modifier exp = Modifiers.exp(1);
        assertFalse(staticValue.addModifier(sqrt));
        assertFalse(staticValue.addModifiers(Set.of(sqrt, exp)));
        assertFalse(staticValue.removeModifier(sqrt));
        assertFalse(staticValue.removeModifiers(Set.of(sqrt, exp)));
        assertFalse(staticValue.containsModifier(sqrt));
        assertFalse(staticValue.containsModifiers(Set.of(sqrt, exp)));
        assertFalse(staticValue.addDependency(sqrt));
        assertFalse(staticValue.addDependencies(Set.of(sqrt, exp)));
        assertFalse(staticValue.removeDependency(sqrt));
        assertFalse(staticValue.removeDependencies(Set.of(sqrt, exp)));
        assertFalse(staticValue.containsDependency(sqrt));
        assertFalse(staticValue.containsDependencies(Set.of(sqrt, exp)));
        assertFalse(staticValue.containsDependencyRecursive(sqrt));
        assertFalse(staticValue.containsDependenciesRecursive(Set.of(sqrt, exp)));
    }

    private static final class ToDoubleTestOperation implements Operation {
        @Override
        public int getParameterCount() {
            return 0;
        }

        @Override
        public Numeral apply(Numeral source, Numeral ... parameters) throws IllegalArgumentException {
            return source.convert(DOUBLE);
        }
    }
}
