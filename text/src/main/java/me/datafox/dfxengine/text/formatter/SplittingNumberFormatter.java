package me.datafox.dfxengine.text.formatter;

import lombok.Data;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.utils.ConfigurationKeys;
import me.datafox.dfxengine.text.utils.TextHandles;
import me.datafox.dfxengine.text.utils.TextUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static me.datafox.dfxengine.text.utils.ConfigurationKeys.*;

/**
 * @author datafox
 */
@Component
public class SplittingNumberFormatter implements NumberFormatter {
    public static final Split[] TIME_SHORT = new Split[] {
            Split.of(BigDecimal.ONE, "s"),
            Split.of(new BigDecimal("60"), "m"),
            Split.of(new BigDecimal("3600"), "h"),
            Split.of(new BigDecimal("86400"), "d"),
            Split.of(new BigDecimal("604800"), "w"),
            Split.of(new BigDecimal("2592000"), "mo"),
            Split.of(new BigDecimal("31536000"), "y")};

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

    @Inject
    public SplittingNumberFormatter(Logger logger, TextHandles handles) {
        this.logger = logger;
        handle = handles.getSplittingNumberFormatter();
        if(FORMATTER.getDefaultValue() == null) {
            FORMATTER.setDefaultValue(handles.getSimpleNumberFormatter());
        }
    }

    @Override
    public String format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        Split[] splits = configuration.get(SPLITS);
        NumberFormatter delegate = factory.getNumberFormatter(configuration.get(FORMATTER));
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
                if(TextUtils.isZero(formatted)) {
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

    @Data
    public static class Split {
        private final BigDecimal multiplier;
        private final String singular;
        private final String plural;

        public static Split of(BigDecimal multiplier, String name) {
            return new Split(multiplier, name, name);
        }

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
