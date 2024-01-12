package me.datafox.dfxengine.values.test;

import org.junit.jupiter.api.BeforeEach;

import static me.datafox.dfxengine.values.test.TestHandles.initializeHandles;
import static me.datafox.dfxengine.values.test.TestValues.initializeValues;

/**
 * @author datafox
 */
public class ValueMapTest {
    @BeforeEach
    public void beforeEach() {
        initializeHandles();
        initializeValues();
    }
}
