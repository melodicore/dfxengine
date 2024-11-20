package me.datafox.dfxengine.text.test;

import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;
import me.datafox.dfxengine.text.formatter.EvenLengthNumberFormatter;
import me.datafox.dfxengine.text.formatter.SimpleNumberFormatter;
import me.datafox.dfxengine.text.formatter.SplittingNumberFormatter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author datafox
 */
public class NumberFormatterTest extends AbstractFormatterTest {
    @Test
    public void simpleNumberFormatterTest() {
        formatter = factory.getNumberFormatter(handles.getSimpleNumberFormatter());

        assertEquals("0", format("0"));
        assertEquals("0", format((BigDecimal) null));

        assertEquals("-52.5474", format("-5.25474e+1"));

        assertEquals("1.23457", format("1.23456789"));
        configuration.set(SimpleNumberFormatter.PRECISION, 7);
        assertEquals("1.234568", format("1.23456789"));

        assertEquals("419.68", format("4.1968e+2"));
        assertEquals("0.0607", format("6.070e-2"));
        assertEquals("4.1968e3", format("4.1968e+3"));
        assertEquals("6.07e-3", format("6.070e-3"));
        configuration.set(SimpleNumberFormatter.MIN_EXPONENT, 4);
        assertEquals("4196.8", format("4.1968e+3"));
        assertEquals("0.00607", format("6.070e-3"));

        assertEquals("0", format("0.0"));
        configuration.set(SimpleNumberFormatter.STRIP_ZEROS, false);
        assertEquals("0.0", format("0.0"));

        configuration.set(SimpleNumberFormatter.PRECISION, 3);
        assertThrows(ConfigurationException.class, () -> format("0"));
        configuration.set(SimpleNumberFormatter.PRECISION, 7);

        configuration.set(SimpleNumberFormatter.MIN_EXPONENT, -1);
        assertThrows(ConfigurationException.class, () -> format("0"));
        configuration.set(SimpleNumberFormatter.MIN_EXPONENT, 4);

        configuration.set(SimpleNumberFormatter.PRECISION, 0);
        assertThrows(ConfigurationException.class, () -> format("0"));
    }

    @Test
    public void evenLengthNumberFormatterTest() {
        formatter = factory.getNumberFormatter(handles.getEvenLengthNumberFormatter());

        assertEquals("0.000000", format("0"));
        assertEquals("0.000000", format((BigDecimal) null));

        assertEquals("-52.5474", format("-5.25474e+1"));

        assertEquals("1.234568", format("1.23456789"));
        configuration.set(EvenLengthNumberFormatter.LENGTH, 7);
        assertEquals("1.23457", format("1.23456789"));

        assertEquals("419.680", format("4.1968e+2"));
        assertEquals("0.06070", format("6.070e-2"));
        assertEquals("4.197e3", format("4.1968e+3"));
        assertEquals("6.07e-3", format("6.070e-3"));
        assertEquals("-4.20e3", format("-4.1968e+3"));
        assertEquals("-6.1e-3", format("-6.070e-3"));
        configuration.set(EvenLengthNumberFormatter.MIN_EXPONENT, 4);
        assertEquals("4196.80", format("4.1968e+3"));
        assertEquals("0.00607", format("6.070e-3"));

        assertEquals("0.00000", format("0.0"));
        configuration.set(EvenLengthNumberFormatter.PAD_ZEROS, false);
        assertEquals("0", format("0.0"));

        configuration.set(EvenLengthNumberFormatter.LENGTH, 3);
        assertThrows(ConfigurationException.class, () -> format("0"));
        configuration.set(EvenLengthNumberFormatter.LENGTH, 7);

        configuration.set(EvenLengthNumberFormatter.MIN_EXPONENT, -1);
        assertThrows(ConfigurationException.class, () -> format("0"));
        configuration.set(EvenLengthNumberFormatter.MIN_EXPONENT, 4);

        configuration.set(EvenLengthNumberFormatter.LENGTH, 0);
        assertThrows(ConfigurationException.class, () -> format("0"));
    }

    @Test
    public void splittingNumberFormatterTest() {
        formatter = factory.getNumberFormatter(handles.getSplittingNumberFormatter());

        assertEquals("1 second", format("1"));
        assertEquals("15 seconds", format("15"));
        assertEquals("2 minutes and 38 seconds", format("158"));
        assertEquals("26 minutes and 27 seconds", format("1587"));
        assertEquals("4 hours, 24 minutes and 32 seconds", format("15872"));
        assertEquals("1 day, 20 hours, 5 minutes and 24 seconds", format("158724"));
        assertEquals("2 weeks, 4 days, 8 hours, 54 minutes and 4 seconds", format("1587244"));
        assertEquals("6 months, 3 days, 17 hours and 40 seconds", format("15872440"));
        assertEquals("5 years, 1 week, 5 days, 2 hours, 6 minutes and 43 seconds", format("158724403"));
        assertEquals("50 years, 4 months, 21 hours, 7 minutes and 19 seconds", format("1587244039"));
        assertEquals("503 years, 3 months, 3 weeks, 2 days, 19 hours, 13 minutes and 12 seconds", format("15872440392"));
        assertEquals("5.033e3 years, 1 month, 1 week, 6 days, 12 minutes and 0 seconds", format("158724403920"));

        configuration.set(SplittingNumberFormatter.SPLITS, SplittingNumberFormatter.TIME_SHORT);
        configuration.set(SplittingNumberFormatter.USE_LIST_DELIMITER, false);
        assertEquals("1s", format("1"));
        assertEquals("15s", format("15"));
        assertEquals("2m 38s", format("158"));
        assertEquals("26m 27s", format("1587"));
        assertEquals("4h 24m 32s", format("15872"));
        assertEquals("1d 20h 5m 24s", format("158724"));
        assertEquals("2w 4d 8h 54m 4s", format("1587244"));
        assertEquals("6mo 3d 17h 40s", format("15872440"));
        assertEquals("5y 1w 5d 2h 6m 43s", format("158724403"));
        assertEquals("50y 4mo 21h 7m 19s", format("1587244039"));
        assertEquals("503y 3mo 3w 2d 19h 13m 12s", format("15872440392"));
        assertEquals("5.033e3y 1mo 1w 6d 12m 0s", format("158724403920"));

        assertEquals("5.033e3y 1mo 1w 6d 12m 0s", format("158724403920.25"));
        configuration.set(SplittingNumberFormatter.ROUND_SMALLEST, false);
        assertEquals("5.033e3y 1mo 1w 6d 12m 0.25s", format("158724403920.25"));

        assertEquals("1m 34s", format("94"));
        configuration.set(SplittingNumberFormatter.FORMATTER, handles.getEvenLengthNumberFormatter());
        assertEquals("1.000000m 34.00000s", format("94"));

        configuration.set(SplittingNumberFormatter.SPLITS, new SplittingNumberFormatter.Split[] {
                SplittingNumberFormatter.Split.of(BigDecimal.ONE, "a"),
                SplittingNumberFormatter.Split.of(BigDecimal.TEN, "b"),
                SplittingNumberFormatter.Split.of(new BigDecimal("5"), "c")});
        assertThrows(ConfigurationException.class, () -> format("42"));
        configuration.set(SplittingNumberFormatter.SPLITS, SplittingNumberFormatter.TIME_SHORT);

        configuration.set(SplittingNumberFormatter.FORMATTER, handles.getSplittingNumberFormatter());
        assertThrows(ConfigurationException.class, () -> format("42"));
    }
}
