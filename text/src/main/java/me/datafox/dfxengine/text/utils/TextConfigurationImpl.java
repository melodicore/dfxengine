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
     * {@link TextFactory} associated with this configuration
     */
    @Getter
    private final TextFactory factory;
    private final Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration;

    /**
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
     * @param key {@inheritDoc}
     * @param value {@inheritDoc}
     * @param <T> {@inheritDoc}
     */
    @Override
    public <T> void set(ConfigurationKey<T> key, ConfigurationValue<T> value) {
        configuration.put(key, value);
    }

    /**
     * @param key {@inheritDoc}
     * @param value {@inheritDoc}
     * @param <T> {@inheritDoc}
     */
    @Override
    public <T> void set(ConfigurationKey<T> key, T value) {
        configuration.put(key, (f, c) -> value);
    }

    /**
     * {@inheritDoc}
     *
     * @param configuration {@inheritDoc}
     */
    @Override
    public void set(TextConfiguration configuration) {
        this.configuration.putAll(configuration.getAll());
    }

    /**
     * @param key {@inheritDoc}
     * @return {@inheritDoc}
     * @param <T> {@inheritDoc}
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
     * @return {@inheritDoc}
     */
    @Override
    public Map<ConfigurationKey<?>,ConfigurationValue<?>> getAll() {
        return Collections.unmodifiableMap(configuration);
    }

    /**
     * @param key {@inheritDoc}
     * @param <T> {@inheritDoc}
     */
    @Override
    public <T> void clear(ConfigurationKey<T> key) {
        configuration.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        configuration.clear();
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public TextConfiguration copy() {
        return new TextConfigurationImpl(factory, getAll());
    }

    /**
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
         * Clears all values registered to the configuration
         *
         * @return this builder
         */
        public Builder clear() {
            configuration.clear();
            return this;
        }

        /**
         * @return {@link TextConfiguration} with the registered values
         */
        public TextConfigurationImpl build() {
            return new TextConfigurationImpl(factory, configuration);
        }
    }
}
