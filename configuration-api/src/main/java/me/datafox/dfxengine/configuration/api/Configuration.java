package me.datafox.dfxengine.configuration.api;

import java.util.Map;

/**
 * A configuration object that may contain any type of value.
 *
 * @author datafox
 */
public interface Configuration {
    /**
     * Associates the specified {@link ConfigurationKey} with the specified {@link ConfigurationValue}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @param <T> type of the object returned by the {@link ConfigurationValue}
     * @return this configuration
     */
    <T> Configuration set(ConfigurationKey<T> key, ConfigurationValue<T> value);

    /**
     * Associates the specified {@link ConfigurationKey} with the specified value.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value object to be used
     * @param <T> type of the object
     * @return this configuration
     */
    <T> Configuration set(ConfigurationKey<T> key, T value);

    /**
     * Applies all entries of the provided configuration to this one, overwriting any existing entries if present.
     *
     * @param configuration {@link Configuration} to be applied
     * @return this configuration
     */
    Configuration set(Configuration configuration);

    /**
     * Returns the value associated with the specified {@link ConfigurationKey}, or its default value if none is
     * present.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param <T> type of the object
     * @return object associated with the key, or the default value if none is present
     */
    <T> T get(ConfigurationKey<T> key);

    /**
     * Returns an unmodifiable {@link Map} containing all entries of this configuration.
     *
     * @return Unmodifiable {@link Map} containing all entries of this configuration
     */
    Map<ConfigurationKey<?>, ConfigurationValue<?>> getAll();

    /**
     * Clears the value associated with the specified {@link ConfigurationKey}.
     *
     * @param key {@link ConfigurationKey} associated with the value to be cleared
     * @param <T> type of the value
     * @return this configuration
     */
    <T> Configuration clear(ConfigurationKey<T> key);

    /**
     * Clears all entries from this configuration.
     *
     * @return this configuration
     */
    Configuration clear();

    /**
     * Returns a unique configuration instance containing all entries of this configuration.
     *
     * @return unique configuration instance containing all entries of this configuration
     */
    Configuration copy();
}
