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

    private ConfigurationImpl(Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration) {
        this.configuration = new HashMap<>(configuration);
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified {@link ConfigurationValue}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @param <T> type of the object returned by the {@link ConfigurationValue}
     */
    @Override
    public <T> void set(ConfigurationKey<T> key, ConfigurationValue<T> value) {
        configuration.put(key, value);
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified value.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value object to be used
     * @param <T> type of the object
     */
    @Override
    public <T> void set(ConfigurationKey<T> key, T value) {
        configuration.put(key, (c) -> value);
    }

    /**
     * Applies all entries of the provided configuration to this one, overwriting any existing entries if present.
     *
     * @param configuration {@link Configuration} to be applied
     */
    @Override
    public void set(Configuration configuration) {
        this.configuration.putAll(configuration.getAll());
    }

    /**
     * Returns the value associated with the specified {@link ConfigurationKey}, or its default value if none is
     * present.
     *
     * @param key {@link ConfigurationKey} to be used
     * @return object associated with the key, or the default value if none is present
     * @param <T> type of the object
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
     */
    @Override
    public <T> void clear(ConfigurationKey<T> key) {
        configuration.remove(key);
    }

    /**
     * Clears all entries from this configuration.
     */
    @Override
    public void clear() {
        configuration.clear();
    }

    /**
     * Returns a unique configuration instance containing all entries of this configuration.
     *
     * @return unique configuration instance containing all entries of this configuration
     */
    @Override
    public Configuration copy() {
        return new ConfigurationImpl(getAll());
    }

    /**
     * Returns a {@link Builder} for a configuration.
     *
     * @return {@link Builder} for a configuration
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link Configuration}.
     */
    public static class Builder {
        private final Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration;

        private Builder() {
            configuration = new HashMap<>();
        }

        /**
         * Registers a {@link ConfigurationKey} and a value to this builder.
         *
         * @param key {@link ConfigurationKey} to be registered to the configuration
         * @param value static value to be associated with the key
         * @return this builder
         * @param <T> type of the value
         */
        public <T> Builder key(ConfigurationKey<T> key, T value) {
            configuration.put(key, (c) -> value);
            return this;
        }

        /**
         * Registers a {@link ConfigurationKey} and a {@link ConfigurationValue} to this builder.
         *
         * @param key {@link ConfigurationKey} to be registered to the configuration
         * @param value {@link ConfigurationValue} to be associated with the key
         * @return this builder
         * @param <T> type of the value
         */
        public <T> Builder key(ConfigurationKey<T> key, ConfigurationValue<T> value) {
            configuration.put(key, value);
            return this;
        }

        /**
         * Clears all values registered to the configuration.
         *
         * @return this builder
         */
        public Builder clear() {
            configuration.clear();
            return this;
        }

        /**
         * Builds a configuration with the registered values.
         *
         * @return configuration with the registered values
         */
        public ConfigurationImpl build() {
            return new ConfigurationImpl(configuration);
        }
    }
}
