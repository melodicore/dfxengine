package me.datafox.dfxengine.text.utils;

import lombok.Getter;
import me.datafox.dfxengine.text.api.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link TextConfiguration}, used by {@link NumberFormatter NumberFormatters},
 * {@link NumberSuffixFormatter NumberSuffixFormatters}, {@link Text Texts} and the {@link TextFactory}.
 *
 * @author datafox
 */
public class TextConfigurationImpl implements TextConfiguration {
    /**
     * {@link TextFactory} associated with this configuration.
     */
    @Getter
    private final TextFactory factory;
    private final Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration;

    /**
     * Public constructor for {@link TextConfigurationImpl}.
     *
     * @param factory {@link TextFactory} to be associated with this configuration
     */
    public TextConfigurationImpl(TextFactory factory) {
        this.factory = factory;
        configuration = new HashMap<>();
    }

    private TextConfigurationImpl(TextFactory factory, Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration) {
        this.factory = factory;
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
        configuration.put(key, (f, c) -> value);
    }

    /**
     * Applies all entries of the provided configuration to this one, overwriting any existing entries if present.
     *
     * @param configuration {@link TextConfiguration} to be applied
     */
    @Override
    public void set(TextConfiguration configuration) {
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
                .map(v -> v.get(factory, this))
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
    public TextConfiguration copy() {
        return new TextConfigurationImpl(factory, getAll());
    }

    /**
     * Returns a {@link Builder} for a configuration.
     *
     * @param factory {@link TextFactory} to be associated with the builder
     * @return {@link Builder} for a configuration
     */
    public static Builder builder(TextFactory factory) {
        return new Builder(factory);
    }

    /**
     * Builder for {@link TextConfiguration}.
     */
    public static class Builder {
        private final TextFactory factory;
        private final Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration;

        private Builder(TextFactory factory) {
            this.factory = factory;
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
            configuration.put(key, (f, c) -> value);
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
         * Builds a {@link TextConfiguration} with the registered values.
         *
         * @return {@link TextConfiguration} with the registered values
         */
        public TextConfigurationImpl build() {
            return new TextConfigurationImpl(factory, configuration);
        }
    }
}
