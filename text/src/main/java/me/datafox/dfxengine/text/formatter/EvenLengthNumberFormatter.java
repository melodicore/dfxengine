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
     * If {@code true}, the number will be padded with zeros to keep the length of all numbers the same. Otherwise, the
     * output may be shorter than the configured length. The default value is {@code true}.
     */
    public static final ConfigurationKey<Boolean> PAD_ZEROS = ConfigurationKey.of(true);

    private final Logger logger;
    @Getter
    private final Handle handle;

    @Inject
    public EvenLengthNumberFormatter(Logger logger, TextHandles handles) {
        this.logger = logger;
        handle = handles.getEvenLengthNumberFormatter();
    }

    @Override
    public String format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        int length = configuration.get(LENGTH);
        NumberSuffixFactory suffixFactory = factory.getNumberSuffixFactory(configuration);
        int exponent = BigDecimalMath.exponent(number);
        int absExponent = Math.abs(exponent);
        String out;
        String suffix = "";
        if(number.signum() == -1) {
            length--;
        }
        if(exponent == length - 1) {
            out = getNumberString(number, length);
        } else if(absExponent >= length || (exponent < 0 && absExponent >= length - 1)) {
            NumberSuffixFactory.Output output = suffixFactory.format(number, factory, configuration);
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
