package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.text.api.NameConverter;
import me.datafox.dfxengine.text.definition.NamedTextDefinition;
import me.datafox.dfxengine.text.definition.NumberTextDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.datafox.dfxengine.text.test.TestConstants.*;
import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author datafox
 */
public class TextDefinitionTest {
    @BeforeEach
    public void beforeEach() {
        initializeConstants();
    }

    @Test
    public void namedTextDefinitionTest() {
        Object object = new Object();
        Object other = new Object();

        textFactory.registerName(object, "name", "names");

        NamedTextDefinition<Object> objectDefinition = new NamedTextDefinition<>(object);
        NamedTextDefinition<Object> otherDefinition = new NamedTextDefinition<>(other);

        assertEquals("name", textFactory.build(objectDefinition));
        assertEquals("names", textFactory.build(objectDefinition,
                textFactory.createEmptyContext().set(SINGULAR, false)));

        assertTrue(textFactory.build(otherDefinition)
                .matches("java\\.lang\\.Object@[0-9a-f]{1,8}"));
        assertTrue(textFactory.build(otherDefinition,
                textFactory.createEmptyContext().set(SINGULAR, false))
                .matches("java\\.lang\\.Object@[0-9a-f]{1,8}s"));

        textFactory.registerNameConverter(new NameConverter<>() {
            @Override
            public String getName(Object value) {
                return "overridden name";
            }

            @Override
            public Class<Object> getObjectClass() {
                return Object.class;
            }
        });

        assertEquals("name", textFactory.build(objectDefinition));
        assertEquals("names", textFactory.build(objectDefinition,
                textFactory.createEmptyContext().set(SINGULAR, false)));

        assertEquals("overridden name", textFactory.build(otherDefinition));
        assertEquals("overridden names", textFactory.build(otherDefinition,
                textFactory.createEmptyContext().set(SINGULAR, false)));
    }

    @Test
    public void numberTextDefinitionTest() {
        NumberTextDefinition definition = new NumberTextDefinition(52);
        NumberTextDefinition other = new NumberTextDefinition(-2417328532141.234919,
                EVEN_LENGTH_NUMBER_FORMATTER_HANDLE_ID);

        assertEquals("52", textFactory.build(definition));
        assertEquals("-2.4E12", textFactory.build(other));
        assertEquals("-2.41733E+12", textFactory.build(other,
                textFactory
                        .createEmptyContext()
                        .set(EVEN_LENGTH_NUMBER_FORMATTER_CHARACTERS, 12)
                        .set(NUMBER_FORMATTER_USE_MANTISSA_PLUS, true)));
    }
}
