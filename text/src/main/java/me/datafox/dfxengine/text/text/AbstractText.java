package me.datafox.dfxengine.text.text;

import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.api.exception.TextConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract {@link Text} class that should be used by all {@link Text} implementations that require
 * {@link TextConfiguration}. The class has a {@link Logger} and optionally an extra {@link TextConfiguration}.
 * {@link #get(TextFactory, TextConfiguration)} appends the extra configuration to the provided one if present, and then
 * calls {@link #generate(TextFactory, TextConfiguration)}, which should be used by extending classes.
 *
 * @author datafox
 */
public abstract class AbstractText implements Text {
    protected final Logger logger;
    protected final TextConfiguration configuration;

    /**
     * @param configuration extra {@link TextConfiguration} to be used by this text
     */
    protected AbstractText(TextConfiguration configuration) {
        logger = LoggerFactory.getLogger(getClass());
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc} Extending classes should not override this method and should use
     * {@link #generate(TextFactory, TextConfiguration)} instead.
     *
     * @param factory {@inheritDoc}
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     *
     * @throws TextConfigurationException {@inheritDoc}
     */
    @Override
    public String get(TextFactory factory, TextConfiguration configuration) {
        if(this.configuration != null) {
            configuration = configuration.copy();
            configuration.set(this.configuration);
        }
        return generate(factory, configuration);
    }

    /**
     * Implementation should use the provided {@link TextConfiguration} and not {@link TextFactory#getConfiguration()}.
     * The reference to {@link TextFactory} is provided for access to objects like {@link Name Names} and
     * {@link NumberFormatter NumberFormatters}.
     *
     * @param factory {@link TextFactory} for generation
     * @param configuration {@link TextConfiguration} for generation
     * @return generated {@link String}
     *
     * @throws TextConfigurationException if the {@link TextConfiguration} is not valid for this text
     */
    protected abstract String generate(TextFactory factory, TextConfiguration configuration);
}
