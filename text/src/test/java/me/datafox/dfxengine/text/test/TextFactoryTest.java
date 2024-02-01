package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.text.definition.SimpleTextDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.datafox.dfxengine.text.test.TestConstants.initializeConstants;
import static me.datafox.dfxengine.text.test.TestConstants.textFactory;
import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class TextFactoryTest {
    @BeforeEach
    public void beforeEach() {
        initializeConstants();
    }

    @Test
    public void test() {
        assertEquals("stringy", textFactory.build(
                new SimpleTextDefinition("stringy"),
                textFactory.createEmptyContext().set(PLURAL_TEXT_PROCESSOR_USE, true)));
        assertEquals("Stringies", textFactory.build(
                new SimpleTextDefinition("stringy"),
                textFactory
                        .createEmptyContext()
                        .set(PLURAL_TEXT_PROCESSOR_USE, true)
                        .set(SINGULAR, false)
                        .set(CASE_TEXT_PROCESSOR_CAPITALIZE, true)));
    }
}
