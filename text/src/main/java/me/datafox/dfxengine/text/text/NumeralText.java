package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.api.exception.TextConfigurationException;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.internal.TextStrings;

import java.util.function.Supplier;

/**
 * A {@link Text} implementation that formats a supplied {@link Numeral} using a {@link NumberFormatter} determined by
 * {@link ConfigurationKeys#NUMBER_FORMATTER}.
 *
 * @author datafox
 */
public class NumeralText extends AbstractText {
    private final Supplier<Numeral> supplier;

    /**
     * Public constructor for {@link NumeralText}.
     *
     * @param supplier {@link Supplier} for the {@link Numeral} to be used
     * @param configuration extra {@link TextConfiguration} to be used by this text
     */
    public NumeralText(Supplier<Numeral> supplier, TextConfiguration configuration) {
        super(configuration);
        this.supplier = supplier;
    }

    /**
     * Public constructor for {@link NumeralText}.
     *
     * @param supplier {@link Supplier} for the {@link Numeral} to be used
     */
    public NumeralText(Supplier<Numeral> supplier) {
        this(supplier, null);
    }

    /**
     * Returns a {@link String}.
     *
     * @param factory {@link TextFactory} for generation
     * @param configuration {@link TextConfiguration} for generation
     * @return generated {@link String}
     *
     * @throws TextConfigurationException if the {@link TextConfiguration} is not valid for this text
     */
    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        if(formatter == null) {
            logger.warn(TextStrings.INVALID_NUMBER_FORMATTER);
            return supplier.get().toString();
        }
        return formatter.format(supplier.get().bigDecValue(), factory, configuration);
    }
}
