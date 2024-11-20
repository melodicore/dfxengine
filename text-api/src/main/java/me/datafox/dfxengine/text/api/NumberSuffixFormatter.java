package me.datafox.dfxengine.text.api;

import lombok.Data;
import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;

import java.math.BigDecimal;

/**
 * A formatter that scales a {@link BigDecimal} number and assigns it a suffix.
 *
 * @author datafox
 */
public interface NumberSuffixFormatter extends Handled {
    /**
     * Formats a {@link BigDecimal} to an {@link Output}.
     *
     * @param number number to format
     * @param factory {@link TextFactory} for formatting
     * @param configuration {@link Configuration} for formatting
     * @return {@link Output} containing the scaled number and a suffix, or the {@link Output} of
     * {@link TextFactory#getDefaultNumberSuffixFormatter()} if the number cannot be formatted by this formatter
     *
     * @throws ConfigurationException if the {@link Configuration} is not valid for this formatter
     */
    Output format(BigDecimal number, TextFactory factory, Configuration configuration);

    /**
     * Returns {@code true} if this formatter can format any {@link BigDecimal} number.
     *
     * @return {@code true} if this formatter can format any {@link BigDecimal} number
     */
    boolean isInfinite();

    /**
     * Data class containing the output of a {@link NumberSuffixFormatter}.
     */
    @Data
    final class Output {
        /**
         * Scaled number.
         */
        private final BigDecimal scaled;

        /**
         * Suffix for the number.
         */
        private final String suffix;
    }
}
