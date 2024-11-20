package me.datafox.dfxengine.configuration.api;

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
}
