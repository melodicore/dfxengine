package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextFactory;
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
     * @param configuration extra {@link Configuration} to be used by this text
     */
    public NumeralText(Supplier<Numeral> supplier, Configuration configuration) {
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
     * @param configuration {@link Configuration} for generation
     * @return generated {@link String}
     *
     * @throws ConfigurationException if the {@link Configuration} is not valid for this text
     */
    @Override
    protected String generate(TextFactory factory, Configuration configuration) {
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        if(formatter == null) {
            logger.warn(TextStrings.INVALID_NUMBER_FORMATTER);
            return supplier.get().toString();
        }
        return formatter.format(supplier.get().bigDecValue(), factory, configuration);
    }
}
