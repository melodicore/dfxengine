package me.datafox.dfxengine.text.api;

import java.util.Map;

/**
 * Configuration used by {@link NumberFormatter NumberFormatters}, {@link NumberSuffixFormatter NumberSuffixFormatters},
 * {@link Text Texts} and the {@link TextFactory}.
 *
 * @author datafox
 */
public interface TextConfiguration {
    /**
     * Returns the {@link TextFactory} associated with this configuration.
     *
     * @return {@link TextFactory} associated with this configuration
     */
    TextFactory getFactory();

    /**
     * Associates the specified {@link ConfigurationKey} with the specified {@link ConfigurationValue}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @param <T> type of the object returned by the {@link ConfigurationValue}
     */
    <T> void set(ConfigurationKey<T> key, ConfigurationValue<T> value);

    /**
     * Associates the specified {@link ConfigurationKey} with the specified value.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value object to be used
     * @param <T> type of the object
     */
    <T> void set(ConfigurationKey<T> key, T value);

    /**
     * Applies all entries of the provided configuration to this one, overwriting any existing entries if present.
     *
     * @param configuration {@link TextConfiguration} to be applied
     */
    void set(TextConfiguration configuration);

    /**
     * Returns the value associated with the specified {@link ConfigurationKey}, or its default value if none is
     * present.
     *
     * @param key {@link ConfigurationKey} to be used
     * @return object associated with the key, or the default value if none is present
     * @param <T> type of the object
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
     */
    <T> void clear(ConfigurationKey<T> key);

    /**
     * Clears all entries from this configuration.
     */
    void clear();

    /**
     * Returns a unique configuration instance containing all entries of this configuration.
     *
     * @return unique configuration instance containing all entries of this configuration
     */
    TextConfiguration copy();
}
