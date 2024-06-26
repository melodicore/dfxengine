package me.datafox.dfxengine.text.utils;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.ConfigurationValue;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author datafox
 */
public class TextConfigurationImpl implements TextConfiguration {
    @Getter
    private final TextFactory factory;
    private final Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration;

    public TextConfigurationImpl(TextFactory factory) {
        this.factory = factory;
        configuration = new HashMap<>();
    }

    private TextConfigurationImpl(TextFactory factory, Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration) {
        this.factory = factory;
        this.configuration = new HashMap<>(configuration);
    }

    @Override
    public <T> void set(ConfigurationKey<T> key, ConfigurationValue<T> value) {
        configuration.put(key, value);
    }

    @Override
    public <T> void set(ConfigurationKey<T> key, T value) {
        configuration.put(key, (f, c) -> value);
    }

    @Override
    public void set(TextConfiguration configuration) {
        this.configuration.putAll(configuration.getAll());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(ConfigurationKey<T> key) {
        return ((ConfigurationValue<T>) configuration
                .getOrDefault(key, (f, c) -> key.getDefaultValue()))
                .get(factory, this);
    }

    @Override
    public Map<ConfigurationKey<?>,ConfigurationValue<?>> getAll() {
        return Collections.unmodifiableMap(configuration);
    }

    @Override
    public <T> void clear(ConfigurationKey<T> key) {
        configuration.remove(key);
    }

    @Override
    public void clear() {
        configuration.clear();
    }

    @Override
    public TextConfiguration copy() {
        return new TextConfigurationImpl(factory, getAll());
    }

    public static Builder builder(TextFactory factory) {
        return new Builder(factory);
    }

    public static class Builder {
        private final TextFactory factory;
        private final Map<ConfigurationKey<?>,ConfigurationValue<?>> configuration;

        private Builder(TextFactory factory) {
            this.factory = factory;
            configuration = new HashMap<>();
        }

        public <T> Builder key(ConfigurationKey<T> key, T value) {
            configuration.put(key, (f, c) -> value);
            return this;
        }

        public <T> Builder key(ConfigurationKey<T> key, ConfigurationValue<T> value) {
            configuration.put(key, value);
            return this;
        }

        public Builder clear() {
            configuration.clear();
            return this;
        }

        public TextConfigurationImpl build() {
            return new TextConfigurationImpl(factory, configuration);
        }
    }

    @Component(order = Integer.MAX_VALUE)
    public static TextConfiguration getDefault(TextFactory factory) {
        return builder(factory).build();
    }
}
