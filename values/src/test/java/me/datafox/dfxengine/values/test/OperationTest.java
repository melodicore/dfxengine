package me.datafox.dfxengine.values.test;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.Operation;
import me.datafox.dfxengine.values.operation.OperationChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.datafox.dfxengine.values.test.TestHandles.initializeHandles;
import static me.datafox.dfxengine.values.test.TestValues.*;

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

        Assertions.assertEquals(Numerals.valueOf(1191099896184766L),
                operation.apply(floatNumeral, intNumeral, Numerals.valueOf(-1e10), intNumeral));
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
