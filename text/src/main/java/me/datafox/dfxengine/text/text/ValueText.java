package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.values.api.Value;

import java.util.function.Supplier;

/**
 * @author datafox
 */
public class ValueText extends SuppliedText<Value> {
    /**
     * If {@code true}, {@link Value#getValue()} will be used instead of {@link Value#getBase()}. The default value is
     * {@code true}.
     */
    public static final ConfigurationKey<Boolean> USE_MODIFIED = ConfigurationKey.of(true);

    public ValueText(Supplier<Value> value, TextConfiguration configuration) {
        super(value, configuration);
    }

    public ValueText(Supplier<Value> value) {
        this(value, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        Numeral numeral = configuration.get(USE_MODIFIED) ? value.get().getValue() : value.get().getBase();
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        if(formatter == null) {
            logger.warn("Invalid number formatter configuration, using Number.toString()");
            return numeral.getNumber().toString();
        }
        return formatter.format(numeral.bigDecValue(), factory, configuration);
    }
}
