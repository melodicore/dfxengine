package me.datafox.dfxengine.text.api;

import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.text.api.exception.TextConfigurationException;

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
     * @param configuration {@link TextConfiguration} for formatting
     * @return {@link String} representation of the number
     *
     * @throws TextConfigurationException if the {@link TextConfiguration} is not valid for this formatter
     */
    String format(BigDecimal number, TextFactory factory, TextConfiguration configuration);
}
