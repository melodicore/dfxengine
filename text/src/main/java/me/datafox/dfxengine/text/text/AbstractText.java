package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract {@link Text} class that should be used by all {@link Text} implementations that require
 * {@link Configuration}. The class has a {@link Logger} and optionally an extra {@link Configuration}.
 * {@link #get(TextFactory, Configuration)} appends the extra configuration to the provided one if present, and then
 * calls {@link #generate(TextFactory, Configuration)}, which should be used by extending classes.
 *
 * @author datafox
 */
public abstract class AbstractText implements Text {
    protected final Logger logger;
    protected final Configuration configuration;

    /**
     * Protected constructor for {@link AbstractText}.
     *
     * @param configuration extra {@link Configuration} to be used by this text
     */
    protected AbstractText(Configuration configuration) {
        logger = LoggerFactory.getLogger(getClass());
        this.configuration = configuration;
    }

    /**
     * Returns a {@link String}. Extending classes should not override this method and should use
     * {@link #generate(TextFactory, Configuration)} instead.
     *
     * @param factory {@link TextFactory} for generation
     * @param configuration {@link Configuration} for generation
     * @return generated {@link String}
     *
     * @throws ConfigurationException if the {@link Configuration} is not valid for this text
     */
    @Override
    public String get(TextFactory factory, Configuration configuration) {
        if(this.configuration != null) {
            configuration = configuration.copy();
            configuration.set(this.configuration);
        }
        return generate(factory, configuration);
    }

    /**
     * Returns a {@link String}.
     *
     * @param factory {@link TextFactory} for generation
     * @param configuration {@link Configuration} for generation
     * @return generated {@link String}
     *
     * @throws ConfigurationException if the {@link Configuration} is not valid for this text
     */
    protected abstract String generate(TextFactory factory, Configuration configuration);
}
