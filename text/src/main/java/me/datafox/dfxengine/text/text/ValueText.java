package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.internal.TextStrings;
import me.datafox.dfxengine.values.api.Value;

import java.util.function.Supplier;

/**
 * A {@link Text} implementation that formats a supplied {@link Numeral} using a {@link NumberFormatter} determined by
 * {@link ConfigurationKeys#NUMBER_FORMATTER}. {@link #USE_MODIFIED} determines if {@link Value#getBase()} or
 * {@link Value#getValue()} should be used.
 *
 * @author datafox
 */
public class ValueText extends AbstractText {
    /**
     * If {@code true}, {@link Value#getValue()} will be used instead of {@link Value#getBase()}. The default value is
     * {@code true}.
     */
    public static final ConfigurationKey<Boolean> USE_MODIFIED = ConfigurationKey.of(true);

    private final Supplier<Value> supplier;

    /**
     * Public constructor for {@link ValueText}.
     *
     * @param supplier {@link Supplier} for the {@link Value} to be used
     * @param configuration extra {@link Configuration} to be used by this text
     */
    public ValueText(Supplier<Value> supplier, Configuration configuration) {
        super(configuration);
        this.supplier = supplier;
    }

    /**
     * Public constructor for {@link ValueText}.
     *
     * @param supplier {@link Supplier} for the {@link Value} to be used
     */
    public ValueText(Supplier<Value> supplier) {
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
        Numeral numeral = configuration.get(USE_MODIFIED) ? supplier.get().getValue() : supplier.get().getBase();
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        if(formatter == null) {
            logger.warn(TextStrings.INVALID_NUMBER_FORMATTER);
            return numeral.getNumber().toString();
        }
        return formatter.format(numeral.bigDecValue(), factory, configuration);
    }
}
