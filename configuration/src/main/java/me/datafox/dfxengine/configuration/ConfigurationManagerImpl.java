package me.datafox.dfxengine.configuration;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.ConfigurationManager;
import me.datafox.dfxengine.configuration.api.ConfigurationValue;
import me.datafox.dfxengine.injector.api.Injector;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;
import me.datafox.dfxengine.injector.api.annotation.Inject;

import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link ConfigurationManager}. This class is designed to be used with the {@link Injector}. All
 * {@link Configuration} instances marked as {@link Component Components} are applied to the configuration of this
 * class. Normal configurations are applied at initialization stage, after all components have been instantiated, but if
 * a component requires configuration at instantiation time, {@link InitialConfiguration} can be used as a component to
 * achieve that.
 *
 * @author datafox
 */
@Component
public class ConfigurationManagerImpl implements ConfigurationManager {
    private final Configuration configuration;

    /**
     * Public constructor for {@link ConfigurationManagerImpl}.
     */
    public ConfigurationManagerImpl() {
        this.configuration = new ConfigurationImpl();
    }

    /**
     * Public constructor for {@link ConfigurationManagerImpl}.
     *
     * @param initialConfigurations initial configurations to be applied
     */
    @Inject
    public ConfigurationManagerImpl(List<InitialConfiguration> initialConfigurations) {
        this();
        initialConfigurations.forEach(this::applyConfiguration);
    }

    /**
     * Clears current {@link Configuration} and applies the provided one to it.
     *
     * @param configuration {@link Configuration} to be applied
     */
    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration.clear();
        applyConfiguration(configuration);
    }

    /**
     * Applies the provided {@link Configuration} to the current one.
     *
     * @param configuration {@link Configuration} to be applied
     */
    @Override
    public void applyConfiguration(Configuration configuration) {
        this.configuration.set(configuration);
    }

    /**
     * Returns the current {@link Configuration}.
     *
     * @return current {@link Configuration}
     */
    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified {@link ConfigurationValue} in the
     * {@link Configuration}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value {@link ConfigurationValue} to be used
     * @return this manager
     */
    @Override
    public <T> ConfigurationManager set(ConfigurationKey<T> key, ConfigurationValue<T> value) {
        configuration.set(key, value);
        return this;
    }

    /**
     * Associates the specified {@link ConfigurationKey} with the specified value in the {@link Configuration}.
     *
     * @param key {@link ConfigurationKey} to be used
     * @param value object to be used
     * @return this manager
     */
    @Override
    public <T> ConfigurationManager set(ConfigurationKey<T> key, T value) {
        configuration.set(key, value);
        return this;
    }

    /**
     * Returns the value associated with the specified {@link ConfigurationKey} in the {@link Configuration}, or its
     * default value if none is present.
     *
     * @param key {@link ConfigurationKey} to be used
     * @return object associated with the key, or the default value if none is present
     */
    @Override
    public <T> T get(ConfigurationKey<T> key) {
        return configuration.get(key);
    }

    /**
     * Returns an unmodifiable {@link Map} containing all entries of the {@link Configuration}.
     *
     * @return Unmodifiable {@link Map} containing all entries of the {@link Configuration}
     */
    @Override
    public Map<ConfigurationKey<?>,ConfigurationValue<?>> getAll() {
        return configuration.getAll();
    }

    /**
     * Clears the value associated with the specified {@link ConfigurationKey} in the {@link Configuration}.
     *
     * @param key {@link ConfigurationKey} associated with the value to be cleared
     * @return this manager
     */
    @Override
    public <T> ConfigurationManager clear(ConfigurationKey<T> key) {
        configuration.clear(key);
        return this;
    }

    /**
     * Clears all entries from the {@link Configuration}.
     *
     * @return this manager
     */
    @Override
    public ConfigurationManager clear() {
        configuration.clear();
        return this;
    }

    /**
     * Applies the specified {@link Configuration Configurations} to this configuration manager.
     *
     * @param configurations {@link Configuration Configurations} to be applied
     */
    @Initialize
    public void initialize(List<Configuration> configurations) {
        configurations.forEach(this::applyConfiguration);
    }
}
