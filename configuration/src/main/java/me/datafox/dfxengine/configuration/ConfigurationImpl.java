package me.datafox.dfxengine.configuration;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.ConfigurationValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link Configuration}.
 *
 * @author datafox
 */
public class ConfigurationImpl implements Configuration {
    private final Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration;

    /**
     * Public constructor for {@link ConfigurationImpl}.
     */
    public ConfigurationImpl() {
        configuration = new HashMap<>();
    }

    /**
     * Protected constructor for {@link ConfigurationImpl}.
     */
    protected ConfigurationImpl(Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration) {
        this.configuration = new HashMap<>(configuration);
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified {@link ConfigurationValue}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @param <T> type of the object returned by the {@link ConfigurationValue}
     * @return this configuration
     */
    @Override
    public <T> ConfigurationImpl set(ConfigurationKey<T> key, ConfigurationValue<T> value) {
        configuration.put(key, value);
        return this;
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified value.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value object to be used
     * @param <T> type of the object
     * @return this configuration
     */
    @Override
    public <T> ConfigurationImpl set(ConfigurationKey<T> key, T value) {
        configuration.put(key, (c) -> value);
        return this;
    }

    /**
     * Applies all entries of the provided configuration to this one, overwriting any existing entries if present.
     *
     * @param configuration {@link Configuration} to be applied
     * @return this configuration
     */
    @Override
    public ConfigurationImpl set(Configuration configuration) {
        this.configuration.putAll(configuration.getAll());
        return this;
    }

    /**
     * Returns the value associated with the specified {@link ConfigurationKey}, or its default value if none is
     * present.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param <T> type of the object
     * @return object associated with the key, or the default value if none is present
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(ConfigurationKey<T> key) {
        return Optional.ofNullable(configuration.get(key))
                .map(v -> v.get(this))
                .map(v -> (T) v)
                .orElseGet(key::getDefaultValue);
    }

    /**
     * Returns an unmodifiable {@link Map} containing all entries of this configuration.
     *
     * @return Unmodifiable {@link Map} containing all entries of this configuration
     */
    @Override
    public Map<ConfigurationKey<?>,ConfigurationValue<?>> getAll() {
        return Collections.unmodifiableMap(configuration);
    }

    /**
     * Clears the value associated with the specified {@link ConfigurationKey}.
     *
     * @param key {@link ConfigurationKey} associated with the value to be cleared
     * @param <T> type of the value
     * @return this configuration
     */
    @Override
    public <T> ConfigurationImpl clear(ConfigurationKey<T> key) {
        configuration.remove(key);
        return this;
    }

    /**
     * Clears all entries from this configuration.
     *
     * @return this configuration
     */
    @Override
    public ConfigurationImpl clear() {
        configuration.clear();
        return this;
    }

    /**
     * Returns a unique configuration instance containing all entries of this configuration.
     *
     * @return unique configuration instance containing all entries of this configuration
     */
    @Override
    public ConfigurationImpl copy() {
        return new ConfigurationImpl(getAll());
    }
}
