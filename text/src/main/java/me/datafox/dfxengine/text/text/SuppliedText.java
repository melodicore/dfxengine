package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.TextConfiguration;

import java.util.function.Supplier;

/**
 * @author datafox
 */
public abstract class SuppliedText<T> extends AbstractText {
    protected final Supplier<T> value;

    protected SuppliedText(Supplier<T> value, TextConfiguration configuration) {
        super(configuration);
        this.value = value;
    }
}
