package me.datafox.dfxengine.values.test;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.modifier.MappingOperationModifier;
import me.datafox.dfxengine.values.modifier.OperationModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.datafox.dfxengine.values.modifier.MappingOperationModifier.resultValue;
import static me.datafox.dfxengine.values.modifier.MappingOperationModifier.sourceValue;
import static me.datafox.dfxengine.values.test.TestHandles.initializeHandles;
import static me.datafox.dfxengine.values.test.TestValues.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class ModifierTest {
    @BeforeEach
    public void beforeEach() {
        initializeHandles();
        initializeValues();
    }
    
    @Test
    public void operationModifierTest() {
        OperationModifier log2 = new OperationModifier(0, Operations::log2);
        OperationModifier power = new OperationModifier(1, Operations::power, StaticValue.of(7.34f));
        OperationModifier lerp = new OperationModifier(2, Operations::lerp, StaticValue.of(2), StaticValue.of(-1));
        OperationModifier divide = new OperationModifier(3, new DivideTestOperation(), StaticValue.of("7.5"));
        assertEquals(0, log2.getPriority());
        assertEquals(0, log2.getDependencies().size());
        assertEquals(Numerals.valueOf(5.1234514e142d), doubleValue.getValue());
        assertTrue(doubleValue.addModifier(lerp));
        assertEquals(Numerals.valueOf(-1.5370354199999998e143d), doubleValue.getValue());
        assertTrue(doubleValue.addModifier(power));
        assertEquals(Numerals.valueOf("-9.232320920332658947832317896633030e+1047"), doubleValue.getValue());
        assertTrue(doubleValue.addModifier(log2));
        assertEquals(Numerals.valueOf(-1.3116292617697139e20d), doubleValue.getValue());
        assertTrue(doubleValue.addModifier(divide));
        assertEquals(Numerals.valueOf("-17488390156929518666.66666666666667"), doubleValue.getValue());
    }

    @Test
    public void mappingOperationModifierTest() {
        MappingOperationModifier modifier = MappingOperationModifier.builder(0)
                .operation(Operations::exp, sourceValue())
                .operation(Operations::multiply, sourceValue(), StaticValue.of(0.62f))
                .operation(Operations::lerp, resultValue(0), StaticValue.of(0), resultValue(1))
                .operation(new DivideTestOperation(), resultValue(2), sourceValue())
                .build();
        assertEquals(Numerals.valueOf(99456), intValue.getValue());
        assertTrue(intValue.addModifier(modifier));
        assertEquals(Numerals.valueOf("9.646812985527996160915179597332683e+43192"), intValue.getValue());

        assertThrows(IllegalArgumentException.class,
                () -> MappingOperationModifier.builder(0).operation(new DivideTestOperation()).build());
    }

    private static class DivideTestOperation implements Operation {
        @Override
        public int getParameterCount() {
            return 1;
        }

        @Override
        public Numeral apply(Numeral source, Numeral ... parameters) throws IllegalArgumentException {
            return Operations.divide(source, parameters[0]);
        }
    }
}
