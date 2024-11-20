package me.datafox.dfxengine.configuration.api;

/**
 * A value used by {@link ConfigurationManager} for configuration. {@link #get(Configuration)} will be called every
 * time the configuration value is requested. Implementations should be careful when using the provided
 * {@link Configuration} to avoid cyclic method calls.
 *
 * @author datafox
 */
@FunctionalInterface
public interface ConfigurationValue<T> {
    /**
     * Returns the configured value. When writing implementations, the developer should be careful when using the
     * provided {@link Configuration} to avoid cyclic method calls
     *
     * @param configuration {@link Configuration} for generating the configuration value
     * @return configured value, or {@code null} if the value cannot be generated
     */
    T get(Configuration configuration);
}
