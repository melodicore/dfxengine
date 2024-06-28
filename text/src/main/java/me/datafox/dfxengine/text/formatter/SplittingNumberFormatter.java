package me.datafox.dfxengine.text.formatter;

import lombok.Data;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.api.exception.TextConfigurationException;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.TextHandles;
import me.datafox.dfxengine.text.utils.TextUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static me.datafox.dfxengine.text.utils.ConfigurationKeys.*;

/**
 * <p>
 * A {@link NumberFormatter} that splits a number into multiple parts in order of magnitude and outputs them in a list.
 * The most common use case is formatting a period of time. A {@link Split} consists of a {@link BigDecimal} multiplier
 * and a singular and plural forms of a suffix. Splits are stored in an array and configured with {@link #SPLITS}. The
 * array must have the splits in ascending order based on the multiplier, and the first split is recommended (but not
 * required) to be {@link BigDecimal#ONE}.
 * </p>
 * <p>
 * Each part of the output will be formatted by a different {@link NumberFormatter} which can be configured with
 * {@link #FORMATTER}. The least significant part of the output can be in decimal form or rounded down, configured with
 * {@link #ROUND_SMALLEST}. When formatting the parts either {@link ConfigurationKeys#DELIMITER}, or
 * {@link ConfigurationKeys#LIST_DELIMITER} and {@link ConfigurationKeys#LIST_LAST_DELIMITER} can be used, configured
 * with {@link #USE_LIST_DELIMITER}.
 * </p>
 *
 * @author datafox
 */
@Component
public class SplittingNumberFormatter implements NumberFormatter {
    /**
     * Array of {@link Split Splits} for formatting time with abbreviations of different units of time, from seconds to
     * years. A month is assumed to be 30 days and a year is assumed to be 365 days.
     */
    public static final Split[] TIME_SHORT = new Split[] {
            Split.of(BigDecimal.ONE, "s"),
            Split.of(new BigDecimal("60"), "m"),
            Split.of(new BigDecimal("3600"), "h"),
            Split.of(new BigDecimal("86400"), "d"),
            Split.of(new BigDecimal("604800"), "w"),
            Split.of(new BigDecimal("2592000"), "mo"),
            Split.of(new BigDecimal("31536000"), "y")};

    /**
     * Array of {@link Split Splits} for formatting time with singular and plural names of different units of time,
     * from seconds to years. A month is assumed to be 30 days and a year is assumed to be 365 days.
     */
    public static final Split[] TIME_LONG = new Split[] {
            Split.of(BigDecimal.ONE, " second", " seconds"),
            Split.of(new BigDecimal("60"), " minute", " minutes"),
            Split.of(new BigDecimal("3600"), " hour", " hours"),
            Split.of(new BigDecimal("86400"), " day", " days"),
            Split.of(new BigDecimal("604800"), " week", " weeks"),
            Split.of(new BigDecimal("2592000"), " month", " months"),
            Split.of(new BigDecimal("31536000"), " year", " years")};

    /**
     * {@link Split Splits} to be used. The elements must be in ascending order by {@link Split#getMultiplier()}, and
     * the first element is recommended to be {@link BigDecimal#ONE}. The default value is {@link #TIME_LONG}.
     */
    public static final ConfigurationKey<Split[]> SPLITS = ConfigurationKey.of(TIME_LONG);

    /**
     * {@link Handle} of the {@link NumberFormatter} to be used for the splits. The default value is
     * {@link TextHandles#getSimpleNumberFormatter()}.
     */
    public static final ConfigurationKey<Handle> FORMATTER = ConfigurationKey.of();

    /**
     * If {@code true}, the last split will be rounded down. The default value is {@code true}.
     */
    public static final ConfigurationKey<Boolean> ROUND_SMALLEST = ConfigurationKey.of(true);

    /**
     * If {@code true}, {@link ConfigurationKeys#LIST_DELIMITER} and {@link ConfigurationKeys#LIST_LAST_DELIMITER} will
     * be used instead of {@link ConfigurationKeys#DELIMITER}. The default value is {@code true}.
     */
    public static final ConfigurationKey<Boolean> USE_LIST_DELIMITER = ConfigurationKey.of(true);

    private final Logger logger;
    @Getter
    private final Handle handle;

    /**
     * @param logger {@link Logger} for this formatter
     * @param handles {@link TextHandles} to be used for this formatter's {@link Handle}
     */
    @Inject
    public SplittingNumberFormatter(Logger logger, TextHandles handles) {
        this.logger = logger;
        handle = handles.getSplittingNumberFormatter();
        if(FORMATTER.getDefaultValue() == null) {
            FORMATTER.setDefaultValue(handles.getSimpleNumberFormatter());
        }
    }

    /**
     * @param number {@inheritDoc}
     * @param factory {@inheritDoc}
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     *
     * @throws TextConfigurationException {@inheritDoc}
     */
    @Override
    public String format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        if(number == null) {
            number = BigDecimal.ZERO;
        }
        NumberFormatter delegate = factory.getNumberFormatter(configuration.get(FORMATTER));
        Split[] splits = configuration.get(SPLITS);
        validateConfiguration(delegate, splits);
        if(delegate == null) {
            logger.warn("Invalid number formatter configuration, using BigDecimal.toString()");
            delegate = DEFAULT_DELEGATE;
        }
        if(number.compareTo(BigDecimal.ZERO) < 0) {
            logger.warn(String.format("%s is negative, using delegate number formatter as is", number));
            return delegate.format(number, factory, configuration);
        }
        List<String> out = new ArrayList<>(splits.length);
        for(int i = splits.length - 1; i >= 0; i--) {
            Split split = splits[i];
            if(i == 0 || number.compareTo(split.getMultiplier()) > 0) {
                BigDecimal divided = number;
                if(i != 0) {
                    divided = divided.divideToIntegralValue(split.getMultiplier()).setScale(0, RoundingMode.DOWN);
                } else if(configuration.get(ROUND_SMALLEST)) {
                    divided = divided.setScale(0, RoundingMode.DOWN);
                }
                String formatted = delegate.format(divided, factory, configuration);
                if(i != 0 && TextUtils.isZero(formatted)) {
                    continue;
                }
                number = number.remainder(split.getMultiplier());
                if(TextUtils.isOne(formatted)) {
                    out.add(formatted + split.getSingular());
                } else {
                    out.add(formatted + split.getPlural());
                }
            }
        }
        if(configuration.get(USE_LIST_DELIMITER)) {
            return TextUtils.join(configuration.get(LIST_DELIMITER),
                    configuration.get(LIST_LAST_DELIMITER), out);
        } else {
            return String.join(configuration.get(DELIMITER), out);
        }
    }

    private void validateConfiguration(NumberFormatter delegate, Split[] splits) {
        if(delegate != null && (getHandle().equals(delegate.getHandle()) || delegate instanceof SplittingNumberFormatter)) {
            throw LogUtils.logExceptionAndGet(logger,
                    "delegate formatter cannot be a SplittingNumberFormatter",
                    TextConfigurationException::new);
        }
        BigDecimal last = null;
        for(Split split : splits) {
            if(last != null) {
                if(last.compareTo(split.getMultiplier()) >= 0) {
                    throw LogUtils.logExceptionAndGet(logger,
                            "split multipliers are not in ascending order",
                            TextConfigurationException::new);
                }
            }
            last = split.getMultiplier();
        }
    }

    /**
     * Defines a single part to be used with {@link SplittingNumberFormatter}. Contains a {@link BigDecimal} multiplier
     * and singular and plural forms of a suffix.
     */
    @Data
    public static class Split {
        /**
         * Multiplier for this split.
         */
        private final BigDecimal multiplier;

        /**
         * Suffix for this split in singular form.
         */
        private final String singular;

        /**
         * Suffix for this split in plural form.
         */
        private final String plural;

        /**
         * This method uses the name as both singular and plural form. It does <b>not</b> use a {@link PluralConverter}.
         *
         * @param multiplier {@link BigDecimal} multiplier for the split
         * @param name name of the split
         * @return new split with the specified parameters
         */
        public static Split of(BigDecimal multiplier, String name) {
            return new Split(multiplier, name, name);
        }

        /**
         * @param multiplier {@link BigDecimal} multiplier for the split
         * @param singular name of the split in singular form
         * @param plural name of the split in plural form
         * @return new split with the specified parameters
         */
        public static Split of(BigDecimal multiplier, String singular, String plural) {
            return new Split(multiplier, singular, plural);
        }
    }

    private static final NumberFormatter DEFAULT_DELEGATE = new DefaultDelegate();

    private static class DefaultDelegate implements NumberFormatter {
        @Override
        public String format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
            return number.toString();
        }

        @Override
        public Handle getHandle() {
            return null;
        }
    }
}
