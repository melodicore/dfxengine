package me.datafox.dfxengine.text.formatter;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.utils.TextHandles;

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
@Getter
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
     * formatting. The default value is
     * {@code true}.
     */
    public static final ConfigurationKey<Boolean> STRIP_ZEROS = ConfigurationKey.of(true);

    private final Handle handle;

    /**
     * @param handles {@link TextHandles} to be used for this formatter's {@link Handle}
     */
    @Inject
    public SimpleNumberFormatter(TextHandles handles) {
        handle = handles.getSimpleNumberFormatter();
    }

    /**
     * @param number {@inheritDoc}
     * @param factory {@inheritDoc}
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        int precision = configuration.get(PRECISION);
        int minExponent = configuration.get(MIN_EXPONENT);
        NumberSuffixFormatter suffixFactory = factory.getNumberSuffixFormatter(configuration);
        if(suffixFactory == null) {
            suffixFactory = factory.getDefaultNumberSuffixFormatter();
        }
        NumberSuffixFormatter.Output output = suffixFactory.format(number, factory, configuration);
        String suffix = "";
        if(Math.abs(output.getExponent()) >= minExponent) {
            number = output.getScaled();
            suffix = output.getSuffix();
        }
        if(configuration.get(STRIP_ZEROS)) {
            number = number.stripTrailingZeros();
        }
        return number.round(new MathContext(precision)).toPlainString() + suffix;
    }
}
