package me.datafox.dfxengine.text.formatter;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.utils.TextHandles;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * A {@link NumberFormatter} that formats a number in a natural form and ensures that every output is no longer than an
 * arbitrary number of characters, configured with {@link #LENGTH}, and the minimum absolute exponent when a
 * {@link NumberSuffixFormatter} will be used can be configured with {@link #MIN_EXPONENT}. The minimum exponent must be
 * smaller than or equal to the length. The output can be configured to be padded with zeros to keep all outputs at the
 * same length with {@link #PAD_ZEROS}. If an output of the desired length cannot be produced, the output will be longer
 * and a warning will be logged.
 *
 * @author datafox
 */
@Component
public class EvenLengthNumberFormatter implements NumberFormatter {
    /**
     * Number of characters in the output. It is recommended to use a value of at least {@code 7}. The default value is
     * {@code 8}.
     */
    public static final ConfigurationKey<Integer> LENGTH = ConfigurationKey.of(8);

    /**
     * Minimum absolute exponent to be formatted with a suffix. It must be smaller than or equal to {@link #LENGTH}.
     * The default value is {@code 3}.
     */
    public static final ConfigurationKey<Integer> MIN_EXPONENT = ConfigurationKey.of(3);

    /**
     * If {@code true}, the number will be padded with zeros to keep the length of all numbers the same. Otherwise, the
     * output may be shorter than the configured length. The default value is {@code true}.
     */
    public static final ConfigurationKey<Boolean> PAD_ZEROS = ConfigurationKey.of(true);

    private final Logger logger;
    @Getter
    private final Handle handle;

    /**
     * @param logger {@link Logger} for this formatter
     * @param handles {@link TextHandles} to be used for this formatter's {@link Handle}
     */
    @Inject
    public EvenLengthNumberFormatter(Logger logger, TextHandles handles) {
        this.logger = logger;
        handle = handles.getEvenLengthNumberFormatter();
    }

    /**
     * @param number {@inheritDoc}
     * @param factory {@inheritDoc}
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        int length = configuration.get(LENGTH);
        int minExp = configuration.get(MIN_EXPONENT);
        NumberSuffixFormatter suffixFactory = factory.getNumberSuffixFormatter(configuration);
        int exponent = BigDecimalMath.exponent(number);
        int absExponent = Math.abs(exponent);
        String out;
        String suffix = "";
        if(number.signum() == -1) {
            length--;
        }
        if(exponent == length - 1 && length == minExp) {
            out = getNumberString(number, length);
        } else if(absExponent >= minExp || (exponent < 0 && absExponent >= minExp - 1)) {
            NumberSuffixFormatter.Output output = suffixFactory.format(number, factory, configuration);
            suffix = output.getSuffix();
            int exp = Math.abs(BigDecimalMath.exponent(output.getScaled()));
            if(exp == length - suffix.length() - 1) {
                out = getNumberString(output.getScaled(), number, length - suffix.length());
            } else {
                out = getNumberString(output.getScaled(), number, length - suffix.length() - 1);
            }
        } else if(exponent < 0) {
            out = getNumberString(number, length - 1 + exponent);
        } else {
            out = getNumberString(number, length - 1);
        }
        if(configuration.get(PAD_ZEROS)) {
            if(number.signum() == -1) {
                length++;
            }
            if(out.length() + suffix.length() < length) {
                if(out.contains(".")) {
                    out += "0".repeat(length - (out.length() + suffix.length()));
                } else {
                    out += "." + "0".repeat(length - (out.length() + suffix.length()) - 1);
                }
            }
        }
        return out + suffix;
    }

    private String getNumberString(BigDecimal number, BigDecimal original, int precision) {
        if(precision < 1) {
            logger.warn(String.format("%s takes up more character than the desired length", original));
            precision = 1;
        }
        return number.round(new MathContext(precision)).toPlainString();
    }

    private String getNumberString(BigDecimal number, int precision) {
        return getNumberString(number, number, precision);
    }
}
