package me.datafox.dfxengine.text.formatter;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;
import me.datafox.dfxengine.configuration.api.ConfigurationKey;
import me.datafox.dfxengine.configuration.api.Configuration;
import me.datafox.dfxengine.configuration.api.exception.ConfigurationException;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.NumberFormatter;
import me.datafox.dfxengine.text.api.NumberSuffixFormatter;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.TextHandles;
import me.datafox.dfxengine.text.utils.internal.TextStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * A {@link NumberFormatter} that formats a number in a natural form. The precision of the number can be configured with
 * {@link #PRECISION}, and the minimum absolute exponent when a {@link NumberSuffixFormatter} will be used can be
 * configured with {@link #MIN_EXPONENT}. The minimum exponent must be smaller than or equal to the precision. Any
 * trailing zeros in the decimal part of the number can be stripped, configured by {@link #STRIP_ZEROS}.
 *
 * @author datafox
 */
@Component
public class SimpleNumberFormatter implements NumberFormatter {
    /**
     * Precision, equivalent to {@link MathContext#getPrecision()}. The default value is {@code 6}.
     */
    public static final ConfigurationKey<Integer> PRECISION = ConfigurationKey.of(6);

    /**
     * Minimum absolute exponent to be formatted with a suffix. It must be smaller than or equal to {@link #PRECISION}.
     * The default value is {@code 3}.
     */
    public static final ConfigurationKey<Integer> MIN_EXPONENT = ConfigurationKey.of(3);

    /**
     * If {@code true}, the number will be stripped of any trailing zeros in the decimal part of the number before
     * formatting. The default value is {@code true}.
     */
    public static final ConfigurationKey<Boolean> STRIP_ZEROS = ConfigurationKey.of(true);

    private final Logger logger;

    /**
     * Identifying {@link Handle} of this formatter.
     */
    @Getter
    private final Handle handle;

    /**
     * Public constructor for {@link SimpleNumberFormatter}.
     *
     * @param logger {@link Logger} for this formatter
     * @param handles {@link TextHandles} to be used for this formatter's {@link Handle}
     */
    @Inject
    public SimpleNumberFormatter(Logger logger, TextHandles handles) {
        this.logger = logger;
        handle = handles.getSimpleNumberFormatter();
    }

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
    @Override
    public String format(BigDecimal number, TextFactory factory, Configuration configuration) {
        if(number == null) {
            number = BigDecimal.ZERO;
        }
        int precision = configuration.get(PRECISION);
        int minExponent = configuration.get(MIN_EXPONENT);
        validateConfiguration(precision, minExponent);
        NumberSuffixFormatter suffixFactory = factory.getNumberSuffixFormatter(configuration);
        if(suffixFactory == null) {
            suffixFactory = factory.getDefaultNumberSuffixFormatter();
        }
        NumberSuffixFormatter.Output output = suffixFactory.format(number, factory, configuration);
        String suffix = "";
        if(Math.abs(BigDecimalMath.exponent(number)) >= minExponent) {
            number = output.getScaled();
            suffix = output.getSuffix();
        }
        if(configuration.get(STRIP_ZEROS)) {
            number = number.stripTrailingZeros();
        }
        return number.round(new MathContext(precision)).toPlainString() + suffix;
    }

    private void validateConfiguration(int precision, int minExponent) {
        if(precision < 1) {
            throw LogUtils.logExceptionAndGet(logger,
                    TextStrings.snfInvalidPrecision(precision),
                    ConfigurationException::new);
        }
        if(minExponent < 0) {
            throw LogUtils.logExceptionAndGet(logger,
                    TextStrings.snfInvalidMinExponent(precision),
                    ConfigurationException::new);
        }
        if(precision < minExponent) {
            throw LogUtils.logExceptionAndGet(logger,
                    TextStrings.snfPrecisionMinExponentMismatch(precision, minExponent),
                    ConfigurationException::new);
        }
    }
}
