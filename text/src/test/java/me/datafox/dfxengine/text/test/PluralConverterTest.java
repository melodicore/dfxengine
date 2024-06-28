package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.text.api.PluralConverter;
import me.datafox.dfxengine.text.converter.DefaultPluralConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author datafox
 */
public class PluralConverterTest {
    @Test
    public void defaultPluralConverterTest() {
        PluralConverter converter = new DefaultPluralConverter();
        assertEquals("balls", converter.convert("ball"));
        assertEquals("masses", converter.convert("mass"));
        assertEquals("axes", converter.convert("axe"));
        assertEquals("axes", converter.convert("ax"));
        assertEquals("leaches", converter.convert("leach"));
        assertEquals("trolleys", converter.convert("trolley"));
        assertEquals("ponies", converter.convert("pony"));
        assertEquals("secretaries", converter.convert("secretary"));
        assertNull(converter.convert(null));
    }
}