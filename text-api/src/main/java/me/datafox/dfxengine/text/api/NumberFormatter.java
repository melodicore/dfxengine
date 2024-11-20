package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;

import java.math.BigDecimal;

/**
 * A formatter that formats a {@link BigDecimal} number to a {@link String}. Should generally use the configured
 * {@link NumberSuffixFormatter} for the operation.
 *
 * @author datafox
 */
public interface NumberFormatter extends Handled {
    /**
     * Formats a {@link BigDecimal} to a {@link String}.
     *
     * @param number number to be formatter
     * @param factory {@link TextFactory} for formatting
     * @param configuration {@link Configuration} for formatting
     * @return {@link String} representation of the number
     *
     * @throws ConfigurationException if the {@link Configuration} is not valid for this formatter
     */
    String format(BigDecimal number, TextFactory factory, Configuration configuration);
}
