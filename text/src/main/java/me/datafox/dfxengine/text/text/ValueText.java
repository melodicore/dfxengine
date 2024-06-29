package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.*;
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
     * @param supplier {@link Supplier} for the {@link Value} to be used
     * @param configuration extra {@link TextConfiguration} to be used by this text
     */
    public ValueText(Supplier<Value> supplier, TextConfiguration configuration) {
        super(configuration);
        this.supplier = supplier;
    }

    /**
     * @param supplier {@link Supplier} for the {@link Value} to be used
     */
    public ValueText(Supplier<Value> supplier) {
        this(supplier, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        Numeral numeral = configuration.get(USE_MODIFIED) ? supplier.get().getValue() : supplier.get().getBase();
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        if(formatter == null) {
            logger.warn(TextStrings.INVALID_NUMBER_FORMATTER);
            return numeral.getNumber().toString();
        }
        return formatter.format(numeral.bigDecValue(), factory, configuration);
    }
}
