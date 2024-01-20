package me.datafox.dfxengine.text.formatter;

import me.datafox.dfxengine.text.api.Formatter;
import me.datafox.dfxengine.values.api.Value;

/**
 * @author datafox
 */
public class ValueFormatter implements Formatter<Value> {
    private final Formatter<Number> delegate;
    private final boolean base;

    public ValueFormatter(Formatter<Number> delegate, boolean base) {
        this.delegate = delegate;
        this.base = base;
    }

    @Override
    public String format(Value value) {
        if(base) {
            return delegate.format(value.getBase().getNumber());
        } else {
            return delegate.format(value.getValue().getNumber());
        }
    }
}
