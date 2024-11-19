package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.api.exception.TextConfigurationException;

import java.util.function.Function;

/**
 * A {@link Text} implementation that generates the text based on an arbitrary {@link ConfigurationKey} and a
 * {@link Function}.
 *
 * @author datafox
 */
public class ConfigurationText<T> extends AbstractText {
    private final ConfigurationKey<T> key;
    private final Function<T, String> function;

    /**
     * Public constructor for {@link ConfigurationText}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param function {@link Function} to convert the configuration value to a {@link String}
     * @param configuration extra {@link TextConfiguration} to be used by this text
     */
    public ConfigurationText(ConfigurationKey<T> key, Function<T,String> function, TextConfiguration configuration) {
        super(configuration);
        this.key = key;
        this.function = function;
    }

    /**
     * Public constructor for {@link ConfigurationText}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param function {@link Function} to convert the configuration value to a {@link String}
     */
    public ConfigurationText(ConfigurationKey<T> key, Function<T,String> function) {
        this(key, function, null);
    }

    /**
     * Returns a {@link String}.
     *
     * @param factory {@link TextFactory} for generation
     * @param configuration {@link TextConfiguration} for generation
     * @return generated {@link String}
     *
     * @throws TextConfigurationException if the {@link TextConfiguration} is not valid for this text
     */
    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        return function.apply(configuration.get(key));
    }
}
