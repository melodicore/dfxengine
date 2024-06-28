package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.text.api.NameConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author datafox
 */
public class TextFactoryTest extends AbstractTest {
    @Test
    public void nameTest() {
        factory.addNameConverter(new TestNameConverter());

        assertEquals("test", factory.getName("test", false));
        assertEquals("tset", factory.getName("test", true));

        factory.createName("test", "ayyy");
        assertEquals("ayyy", factory.getName("test", false));
        assertEquals("ayyys", factory.getName("test", true));

        factory.setPluralConverter(s -> s + s);
        assertEquals("ayyy", factory.getName("test", false));
        assertEquals("ayyys", factory.getName("test", true));

        factory.createName("test", "ayyy");
        assertEquals("ayyy", factory.getName("test", false));
        assertEquals("ayyyayyy", factory.getName("test", true));
    }

    private static class TestNameConverter implements NameConverter<CharSequence> {
        @Override
        public Class<CharSequence> getType() {
            return CharSequence.class;
        }

        @Override
        public boolean isPluralCapable() {
            return true;
        }

        @Override
        public String convert(CharSequence object) {
            return object.toString();
        }

        @Override
        public String convertPlural(CharSequence object) {
            return new StringBuilder(object).reverse().toString();
        }
    }
}
