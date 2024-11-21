package me.datafox.dfxengine.configuration.api;

import java.util.Map;

/**
 * A singleton class that maintains a {@link Configuration} instance.
 *
 * @author datafox
 */
public interface ConfigurationManager {
    /**
     * Clears current {@link Configuration} and applies the provided one to it.
     *
     * @param configuration {@link Configuration} to be applied
     */
    void setConfiguration(Configuration configuration);

    /**
     * Applies the provided {@link Configuration} to the current one.
     *
     * @param configuration {@link Configuration} to be applied
     */
    void applyConfiguration(Configuration configuration);

    /**
     * Returns the current {@link Configuration}. This method should always return the same configuration instance.
     *
     * @return current {@link Configuration}
     */
    Configuration getConfiguration();

    /**
     * Associates the specified {@link ConfigurationKey} with the specified {@link ConfigurationValue} in the
     * {@link Configuration}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @param <T> type of the object returned by the {@link ConfigurationValue}
     * @return this manager
     */
    <T> ConfigurationManager set(ConfigurationKey<T> key, ConfigurationValue<T> value);

    /**
     * Associates the specified {@link ConfigurationKey} with the specified value in the {@link Configuration}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value object to be used
     * @param <T> type of the object
     * @return this manager
     */
    <T> ConfigurationManager set(ConfigurationKey<T> key, T value);

    /**
     * Returns the value associated with the specified {@link ConfigurationKey} in the {@link Configuration}, or its
     * default value if none is present.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param <T> type of the object
     * @return object associated with the key, or the default value if none is present
     */
    <T> T get(ConfigurationKey<T> key);

    /**
     * Returns an unmodifiable {@link Map} containing all entries of the {@link Configuration}.
     *
     * @return Unmodifiable {@link Map} containing all entries of the {@link Configuration}
     */
    Map<ConfigurationKey<?>, ConfigurationValue<?>> getAll();

    /**
     * Clears the value associated with the specified {@link ConfigurationKey} in the {@link Configuration}.
     *
     * @param key {@link ConfigurationKey} associated with the value to be cleared
     * @param <T> type of the value
     * @return this manager
     */
    <T> ConfigurationManager clear(ConfigurationKey<T> key);

    /**
     * Clears all entries from the {@link Configuration}.
     *
     * @return this manager
     */
    ConfigurationManager clear();
}
