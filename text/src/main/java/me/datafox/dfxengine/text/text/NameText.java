package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.function.Supplier;

/**
 * @author datafox
 */
public class NameText<T> extends SuppliedText<T> {
    /**
     * If {@code true}, the plural form will be used instead of the singular form. The default value is {@code false}.
     */
    public static final ConfigurationKey<Boolean> USE_PLURAL = ConfigurationKey.of(false);

    public NameText(Supplier<T> value, TextConfiguration configuration) {
        super(value, configuration);
    }

    public NameText(Supplier<T> value) {
        this(value, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        return factory.getName(value.get(), configuration.get(USE_PLURAL));
    }
}
