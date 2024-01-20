package me.datafox.dfxengine.values.test;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.operation.MappingOperationChain;
import me.datafox.dfxengine.values.operation.OperationChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.datafox.dfxengine.values.operation.MappingOperationChain.*;
import static me.datafox.dfxengine.values.test.TestHandles.initializeHandles;
import static me.datafox.dfxengine.values.test.TestValues.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author datafox
 */
public class OperationTest {
    @BeforeEach
    public void beforeEach() {
        initializeHandles();
        initializeValues();
    }

    @Test
    public void operationChainTest() {
        OperationChain operation = OperationChain.builder()
                .operation(Operations::cbrt)
                .operation(Operations::divide)
                .operation(Operations::lerp)
                .operation(new ToIntegerTestOperation())
                .build();

        assertEquals(Numerals.of(1191099896184766L),
                operation.apply(floatNumeral, intNumeral, Numerals.of(-1e10), intNumeral));

        assertThrows(IllegalArgumentException.class, () -> operation.apply(floatNumeral, intNumeral));
    }

    @Test
    public void mappingOperationChainTest() {
        MappingOperationChain operation = builder()
                .operation(Operations::cbrt)
                .operation(Operations::divide)
                .operation(Operations::lerp)
                .operation(new ToIntegerTestOperation())
                .build();

        assertEquals(Numerals.of(1191099896184766L),
                operation.apply(floatNumeral,
                        sourceNumeral(),
                        resultNumeral(0), intNumeral,
                        resultNumeral(1), Numerals.of(-1e10), intNumeral,
                        resultNumeral(2)));

        assertThrows(IllegalArgumentException.class, () -> operation.apply(floatNumeral,
                        sourceNumeral(),
                        resultNumeral(1), intNumeral,
                        resultNumeral(2), Numerals.of(-1e10), intNumeral,
                        resultNumeral(3)));

        assertThrows(IllegalArgumentException.class, () -> operation.apply(floatNumeral, intNumeral, Numerals.of(-1e10), intNumeral));
    }

    private static class ToIntegerTestOperation implements Operation {
        @Override
        public int getParameterCount() {
            return 0;
        }

        @Override
        public Numeral apply(Numeral source, Numeral ... parameters) throws IllegalArgumentException {
            return source.toInteger();
        }
    }
}
