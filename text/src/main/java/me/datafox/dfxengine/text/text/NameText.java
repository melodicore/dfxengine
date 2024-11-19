package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.api.exception.TextConfigurationException;

import java.util.function.Supplier;

/**
 * A text implementation that takes a supplied object and returns its name by using
 * {@link TextFactory#getName(Object, boolean)}. {@link #USE_PLURAL} determines if the singular or plural form of the
 * {@link Name} should be used.
 *
 * @author datafox
 */
public class NameText<T> extends AbstractText {
    /**
     * If {@code true}, the plural form will be used instead of the singular form. The default value is {@code false}.
     */
    public static final ConfigurationKey<Boolean> USE_PLURAL = ConfigurationKey.of(false);

    private final Supplier<T> supplier;

    /**
     * Public constructor for {@link NameText}.
     *
     * @param supplier {@link Supplier} for the {@link T} to be used
     * @param configuration extra {@link TextConfiguration} to be used by this text
     */
    public NameText(Supplier<T> supplier, TextConfiguration configuration) {
        super(configuration);
        this.supplier = supplier;
    }

    /**
     * Public constructor for {@link NameText}.
     *
     * @param supplier {@link Supplier} for the value to be used
     */
    public NameText(Supplier<T> supplier) {
        this(supplier, null);
    }

    /**
     * Returns a {@link String}.
     *
     * @throws TextConfigurationException if the {@link TextConfiguration} is not valid for this text
     */
    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        return factory.getName(supplier.get(), configuration.get(USE_PLURAL));
    }
}
