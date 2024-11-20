package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;

/**
 * A {@link String} generation interface.
 *
 * @author datafox
 */
@FunctionalInterface
public interface Text {
    /**
     * Returns a {@link String}.
     *
     * @param factory {@link TextFactory} for generation
     * @param configuration {@link Configuration} for generation
     * @return generated {@link String}
     *
     * @throws ConfigurationException if the {@link Configuration} is not valid for this text
     */
    String get(TextFactory factory, Configuration configuration);
}
