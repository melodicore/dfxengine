package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.function.Function;

/**
 * @author datafox
 */
public class ConfigurationText<T> extends AbstractText {
    private final ConfigurationKey<T> key;
    private final Function<T, String> function;

    public ConfigurationText(ConfigurationKey<T> key, Function<T,String> function, TextConfiguration configuration) {
        super(configuration);
        this.key = key;
        this.function = function;
    }

    public ConfigurationText(ConfigurationKey<T> key, Function<T,String> function) {
        this(key, function, null);
    }

    @Override
    protected String generate(TextFactory factory, TextConfiguration configuration) {
        return function.apply(configuration.get(key));
    }
}
