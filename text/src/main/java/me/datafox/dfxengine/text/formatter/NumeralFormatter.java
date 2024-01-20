package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.text.api.Formatter;

/**
 * @author datafox
 */
public class NumeralFormatter implements Formatter<Numeral> {
    private final Formatter<Number> delegate;

    public NumeralFormatter(Formatter<Number> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String format(Numeral numeral) {
        return delegate.format(numeral.getNumber());
    }
}
