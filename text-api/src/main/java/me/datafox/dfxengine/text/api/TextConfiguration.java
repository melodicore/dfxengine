package me.datafox.dfxengine.text.api;

import java.util.Map;

/**
 * @author datafox
 */
public interface TextConfiguration {
    TextFactory getFactory();

    <T> void set(ConfigurationKey<T> key, ConfigurationValue<T> value);

    <T> void set(ConfigurationKey<T> key, T value);

    void set(TextConfiguration configuration);

    <T> T get(ConfigurationKey<T> key);

    Map<ConfigurationKey<?>, ConfigurationValue<?>> getAll();

    <T> void clear(ConfigurationKey<T> key);

    void clear();

    TextConfiguration copy();
}
