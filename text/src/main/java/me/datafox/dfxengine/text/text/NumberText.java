package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 * @author datafox
 */
public class NumberText extends SuppliedText<Number> {
    public NumberText(Supplier<Number> number, TextConfiguration configuration) {
        super(number, configuration);
    }

    public NumberText(Supplier<Number> number) {
        this(number, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        if(formatter == null) {
            logger.warn("Invalid number formatter configuration, using Number.toString()");
            return value.get().toString();
        }
        return formatter.format(new BigDecimal(value.get().toString()), factory, configuration);
    }
}
