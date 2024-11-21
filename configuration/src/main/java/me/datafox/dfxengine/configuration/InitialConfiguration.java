package me.datafox.dfxengine.configuration;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.ConfigurationValue;
import me.datafox.dfxengine.injector.api.Injector;

import java.util.Map;

/**
 * Extension of {@link ConfigurationImpl}. Works exactly the same way but is applied at instantiation when using the
 * {@link Injector}.
 *
 * @author datafox
 */
public class InitialConfiguration extends ConfigurationImpl {
    /**
     * Public constructor for {@link InitialConfiguration}.
     */
    public InitialConfiguration() {
        super();
    }

    private InitialConfiguration(Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration) {
        super(configuration);
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified {@link ConfigurationValue}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @return this configuration
     */
    @Override
    public <T> InitialConfiguration set(ConfigurationKey<T> key, ConfigurationValue<T> value) {
        super.set(key, value);
        return this;
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified value.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value object to be used
     * @return this configuration
     */
    @Override
    public <T> InitialConfiguration set(ConfigurationKey<T> key, T value) {
        super.set(key, value);
        return this;
    }

    /**
     * Applies all entries of the provided configuration to this one, overwriting any existing entries if present.
     *
     * @param configuration {@link Configuration} to be applied
     * @return this configuration
     */
    @Override
    public InitialConfiguration set(Configuration configuration) {
        super.set(configuration);
        return this;
    }

    /**
     * Clears the value associated with the specified {@link ConfigurationKey}.
     *
     * @param key {@link ConfigurationKey} associated with the value to be cleared
     * @return this configuration
     */
    @Override
    public <T> InitialConfiguration clear(ConfigurationKey<T> key) {
        super.clear(key);
        return this;
    }

    /**
     * Clears all entries from this configuration.
     *
     * @return this configuration
     */
    @Override
    public InitialConfiguration clear() {
        super.clear();
        return this;
    }

    /**
     * Returns a unique configuration instance containing all entries of this configuration.
     *
     * @return unique configuration instance containing all entries of this configuration
     */
    @Override
    public InitialConfiguration copy() {
        return new InitialConfiguration(getAll());
    }
}
