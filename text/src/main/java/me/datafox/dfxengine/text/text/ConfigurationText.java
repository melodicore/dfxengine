package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.Text;
import me.datafox.dfxengine.text.api.TextConfiguration;
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
     * @param key {@link ConfigurationKey} to be used
     * @param function {@link Function} to convert the configuration value to a {@link String}
     */
    public ConfigurationText(ConfigurationKey<T> key, Function<T,String> function) {
        this(key, function, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        return function.apply(configuration.get(key));
    }
}
