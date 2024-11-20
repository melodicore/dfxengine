package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;
import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextFactory;

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
     * @param configuration extra {@link Configuration} to be used by this text
     */
    public ConfigurationText(ConfigurationKey<T> key, Function<T,String> function, Configuration configuration) {
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
     * @param configuration {@link Configuration} for generation
     * @return generated {@link String}
     *
     * @throws ConfigurationException if the {@link Configuration} is not valid for this text
     */
    @Override
    protected String generate(TextFactory factory, Configuration configuration) {
        return function.apply(configuration.get(key));
    }
}
