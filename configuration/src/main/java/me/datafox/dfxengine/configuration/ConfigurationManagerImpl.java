package me.datafox.dfxengine.configuration;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.ConfigurationManager;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

import java.util.List;

/**
 * Implementation of {@link ConfigurationManager}.
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
     * Clears current {@link Configuration} and applies the provided one to it.
     *
     * @param configuration {@link Configuration} to be applied
     */
    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration.clear();
        this.configuration.set(configuration);
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
     * Registers the specified {@link Configuration Configurations} to this configuration manager.
     *
     * @param configurations {@link Configuration Configurations} to be registered
     */
    @Initialize
    public void initialize(List<Configuration> configurations) {
        configurations.forEach(this::applyConfiguration);
    }
}
