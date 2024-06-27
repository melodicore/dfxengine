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
     * @return {@link TextFactory} associated with this configuration
     */
    TextFactory getFactory();

    /**
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @param <T> type of the object returned by the {@link ConfigurationValue}
     */
    <T> void set(ConfigurationKey<T> key, ConfigurationValue<T> value);

    /**
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
     * @param key {@link ConfigurationKey} to be used
     * @return object associated with the key, or {@link ConfigurationKey#getDefaultValue()} if none is present
     * @param <T> type of the object
     */
    <T> T get(ConfigurationKey<T> key);

    /**
     * @return Unmodifiable {@link Map} containing all entries of this configuration
     */
    Map<ConfigurationKey<?>, ConfigurationValue<?>> getAll();

    /**
     * @param key {@link ConfigurationKey} associated with the value to be cleared
     * @param <T> type of the value
     */
    <T> void clear(ConfigurationKey<T> key);

    /**
     * Clears all entries from this configuration.
     */
    void clear();

    /**
     * @return unique configuration instance containing all entries of this configuration
     */
    TextConfiguration copy();
}
