package me.datafox.dfxengine.text.api;

/**
 * A value used by {@link TextFactory} for configuration. {@link #get(TextFactory, TextConfiguration)} will be called
 * every time the configuration value is requested. Implementations should be careful when using the provided
 * {@link TextConfiguration} to avoid cyclic method calls.
 *
 * @author datafox
 */
@FunctionalInterface
public interface ConfigurationValue<T> {
    /**
     * When writing implementations, the developer should be careful when using the provided {@link TextConfiguration}
     * to avoid cyclic method calls
     *
     * @param factory {@link TextFactory} for generating the configuration value
     * @param configuration {@link TextConfiguration} for generating the configuration value
     * @return configured value, or {@code null} if the value cannot be generated
     */
    T get(TextFactory factory, TextConfiguration configuration);
}
