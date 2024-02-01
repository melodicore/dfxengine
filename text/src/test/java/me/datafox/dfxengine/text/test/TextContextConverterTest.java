package me.datafox.dfxengine.text.test;

import org.junit.jupiter.api.Test;

import static me.datafox.dfxengine.text.utils.TextFactoryConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class TextContextConverterTest {
    @Test
    public void stringConverterTest() {
        assertEquals("test", STRING_CONVERTER.toString("test"));
        assertEquals("", STRING_CONVERTER.toString(null));

        assertEquals("value", STRING_CONVERTER.toValue("value"));
        assertEquals("", STRING_CONVERTER.toValue(null));
    }

    @Test
    public void booleanConverterTest() {
        assertEquals("true", BOOLEAN_CONVERTER.toString(true));
        assertEquals("false", BOOLEAN_CONVERTER.toString(false));
        assertEquals("false", BOOLEAN_CONVERTER.toString(null));

        assertEquals(true, BOOLEAN_CONVERTER.toValue("true"));
        assertEquals(true, BOOLEAN_CONVERTER.toValue("TRUE"));
        assertEquals(true, BOOLEAN_CONVERTER.toValue("tRuE"));
        assertEquals(false, BOOLEAN_CONVERTER.toValue("false"));
        assertEquals(false, BOOLEAN_CONVERTER.toValue("FALSE"));
        assertEquals(false, BOOLEAN_CONVERTER.toValue("FaLsE"));
        assertEquals(false, BOOLEAN_CONVERTER.toValue("not a boolean"));
        assertEquals(false, BOOLEAN_CONVERTER.toValue(null));
    }

    @Test
    public void integerConverterTest() {
        assertEquals("0", INTEGER_CONVERTER.toString(0));
        assertEquals("4210", INTEGER_CONVERTER.toString(4210));
        assertEquals("-72", INTEGER_CONVERTER.toString(-72));
        assertEquals("0", INTEGER_CONVERTER.toString(null));

        assertEquals(0, INTEGER_CONVERTER.toValue("0"));
        assertEquals(1337, INTEGER_CONVERTER.toValue("1337"));
        assertEquals(-3141, INTEGER_CONVERTER.toValue("-3141"));
        assertEquals(0, INTEGER_CONVERTER.toValue(null));
    }
}
