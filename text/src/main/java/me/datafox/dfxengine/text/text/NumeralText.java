package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.function.Supplier;

/**
 * @author datafox
 */
public class NumeralText extends SuppliedText<Numeral> {
    public NumeralText(Supplier<Numeral> numeral, TextConfiguration configuration) {
        super(numeral, configuration);
    }

    public NumeralText(Supplier<Numeral> numeral) {
        this(numeral, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        NumberFormatter formatter = factory.getNumberFormatter(configuration);
        if(formatter == null) {
            logger.warn("Invalid number formatter configuration, using Numeral.toString()");
            return value.get().toString();
        }
        return formatter.format(value.get().bigDecValue(), factory, configuration);
    }
}
