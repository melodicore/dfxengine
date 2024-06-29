package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.text.api.exception.TextConfigurationException;
import me.datafox.dfxengine.text.formatter.SimpleNumberFormatter;
import me.datafox.dfxengine.text.suffix.CharDigitSuffixFormatter;
import me.datafox.dfxengine.text.suffix.ExponentSuffixFormatter;
import me.datafox.dfxengine.text.suffix.NamedSuffixFormatter;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author datafox
 */
public class NumberSuffixFormatterTest extends AbstractFormatterTest {
    @BeforeEach
    @Override
    public void beforeEach() {
        super.beforeEach();
        formatter = factory.getNumberFormatter(handles.getSimpleNumberFormatter());
        configuration.set(SimpleNumberFormatter.MIN_EXPONENT, 0);
    }

    @Test
    public void charDigitSuffixFormatterTest() {
        configuration.set(ConfigurationKeys.NUMBER_SUFFIX_FORMATTER, handles.getCharDigitSuffixFormatter());

        assertEquals("1.245", format("1.245e0"));
        assertEquals("12.45", format("1.245e1"));
        assertEquals("124.5", format("1.245e2"));
        assertEquals("1.245a", format("1.245e3"));
        assertEquals("12.45b", format("1.245e7"));
        assertEquals("124.5c", format("1.245e11"));
        assertEquals("124.5z", format("1.245e80"));
        assertEquals("1.245aa", format("1.245e81"));
        assertEquals("12.45ab", format("1.245e85"));
        assertEquals("124.5ac", format("1.245e89"));
        assertEquals("124.5zz", format("1.245e2108"));
        assertEquals("1.245aaa", format("1.245e2109"));

        assertEquals("124.5-a", format("1.245e-1"));
        assertEquals("12.45-b", format("1.245e-5"));
        assertEquals("1.245-c", format("1.245e-9"));

        assertEquals("-1.245a", format("-1.245e3"));
        assertEquals("-12.45b", format("-1.245e7"));
        assertEquals("-124.5c", format("-1.245e11"));

        assertEquals("-124.5-a", format("-1.245e-1"));
        assertEquals("-12.45-b", format("-1.245e-5"));
        assertEquals("-1.245-c", format("-1.245e-9"));

        configuration.set(CharDigitSuffixFormatter.INTERVAL, 1);
        assertEquals("5.25", format("5.25e0"));
        assertEquals("5.25a", format("5.25e1"));
        assertEquals("5.25b", format("5.25e2"));

        configuration.set(CharDigitSuffixFormatter.EXPONENT_PLUS, true);
        assertEquals("9.939", format("9.939e0"));
        assertEquals("9.939+a", format("9.939e1"));
        assertEquals("9.939+b", format("9.939e2"));

        configuration.set(CharDigitSuffixFormatter.CHARACTERS, new char[] {'z', 'ö', 'x'});
        assertEquals("5.2933", format("5.2933e0"));
        assertEquals("5.2933+z", format("5.2933e1"));
        assertEquals("5.2933+ö", format("5.2933e2"));
        assertEquals("5.2933+x", format("5.2933e3"));
        assertEquals("5.2933+zz", format("5.2933e4"));
        assertEquals("5.2933+zö", format("5.2933e5"));
        assertEquals("5.2933+zx", format("5.2933e6"));
        assertEquals("5.2933+zzz", format("5.2933e13"));
        assertEquals("5.2933+xöxööxxz", format("5.2933e9001"));

        configuration.set(CharDigitSuffixFormatter.INTERVAL, 0);
        assertThrows(TextConfigurationException.class, () -> format("0"));
        configuration.set(CharDigitSuffixFormatter.INTERVAL, 1);

        configuration.set(CharDigitSuffixFormatter.CHARACTERS, new char[0]);
        assertThrows(TextConfigurationException.class, () -> format("0"));
    }

    @Test
    public void exponentSuffixFormatterTest() {
        configuration.set(ConfigurationKeys.NUMBER_SUFFIX_FORMATTER, handles.getExponentSuffixFormatter());

        assertEquals("2.52689e-2", format("2.52689e-2"));
        assertEquals("2.52689e-1", format("2.52689e-1"));
        assertEquals("2.52689", format("2.52689e0"));
        assertEquals("2.52689e1", format("2.52689e1"));
        assertEquals("2.52689e2", format("2.52689e2"));

        assertEquals("-2.52689e-2", format("-2.52689e-2"));
        assertEquals("-2.52689e-1", format("-2.52689e-1"));
        assertEquals("-2.52689", format("-2.52689e0"));
        assertEquals("-2.52689e1", format("-2.52689e1"));
        assertEquals("-2.52689e2", format("-2.52689e2"));

        configuration.set(ExponentSuffixFormatter.EXPONENT_PLUS, true);
        assertEquals("2.52689", format("2.52689e0"));
        assertEquals("2.52689e+1", format("2.52689e1"));
        assertEquals("2.52689e+2", format("2.52689e2"));
        assertEquals("-2.52689e+1", format("-2.52689e1"));
        assertEquals("-2.52689e+2", format("-2.52689e2"));

        configuration.set(ExponentSuffixFormatter.INTERVAL, 4);
        assertEquals("2526.89", format("2.52689e3"));
        assertEquals("2.52689e+4", format("2.52689e4"));
        assertEquals("25.2689e+4", format("2.52689e5"));
        assertEquals("252.689e+4", format("2.52689e6"));
        assertEquals("2526.89e+4", format("2.52689e7"));
        assertEquals("2.52689e+8", format("2.52689e8"));

        configuration.set(ExponentSuffixFormatter.INTERVAL, 0);
        assertThrows(TextConfigurationException.class, () -> format("0"));
    }

    @Test
    public void namedSuffixFormatterTest() {
        configuration.set(ConfigurationKeys.NUMBER_SUFFIX_FORMATTER, handles.getNamedSuffixFormatter());

        assertEquals("2.52689", format("2.52689e0"));
        assertEquals("25.2689", format("2.52689e1"));
        assertEquals("252.689", format("2.52689e2"));
        assertEquals("2.52689k", format("2.52689e3"));
        assertEquals("25.2689M", format("2.52689e7"));
        assertEquals("252.689B", format("2.52689e11"));
        assertEquals("252.689NOg", format("2.52689e272"));
        assertEquals("2.52689e273", format("2.52689e273"));
        assertEquals("2.52689e-1", format("2.52689e-1"));

        configuration.set(NamedSuffixFormatter.SUFFIXES, NamedSuffixFormatter.SI);
        assertEquals("2.52689G", format("2.52689e9"));
        assertEquals("25.2689T", format("2.52689e13"));
        assertEquals("252.689P", format("2.52689e17"));
        assertEquals("252.689Q", format("2.52689e32"));
        assertEquals("2.52689e33", format("2.52689e33"));

        configuration.set(NamedSuffixFormatter.INTERVAL, 1);
        assertEquals("2.52689", format("2.52689e0"));
        assertEquals("2.52689k", format("2.52689e1"));
        assertEquals("2.52689M", format("2.52689e2"));

        configuration.set(NamedSuffixFormatter.SUFFIXES, new String[0]);
        assertEquals("2.52689e9", format("2.52689e9"));

        configuration.set(NamedSuffixFormatter.INTERVAL, 0);
        assertThrows(TextConfigurationException.class, () -> format("0"));
    }
}