package me.datafox.dfxengine.text.suffix;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.*;
import me.datafox.dfxengine.text.api.exception.TextConfigurationException;
import me.datafox.dfxengine.text.utils.TextHandles;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.math.BigDecimal;

/**
 * A {@link NumberSuffixFormatter} that generates an exponential suffix in an arbitrary base with an arbitrary set of
 * characters representing digits. The character set can be configured with {@link #CHARACTERS}, and {@link #INTERVAL}
 * determines how many powers of ten is required for the exponent to increase. The formatter can also be configured to
 * output a plus sign on positive exponents with {@link #EXPONENT_PLUS}.
 *
 * @author datafox
 */
@Component
public class CharDigitSuffixFormatter implements NumberSuffixFormatter {
    /**
     * 26 English alphabet in lowercase.
     */
    public static final char[] ALPHABET = new char[] {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    /**
     * Characters to be used as digits for the exponent. The default value is {@link #ALPHABET}.
     */
    public static final ConfigurationKey<char[]> CHARACTERS = ConfigurationKey.of(ALPHABET);

    /**
     * Interval for formatted exponents. The default value is {@code 3}.
     */
    public static final ConfigurationKey<Integer> INTERVAL = ConfigurationKey.of(3);

    /**
     * If {@code true}, will output a plus sign on positive exponents. The default value is {@code false}.
     */
    public static final ConfigurationKey<Boolean> EXPONENT_PLUS = ConfigurationKey.of(false);

    private final Logger logger;
    @Getter
    private final Handle handle;

    /**
     * @param logger {@link Logger} for this formatter
     * @param handles {@link TextHandles} to be used for this formatter's {@link Handle}
     */
    @Inject
    public CharDigitSuffixFormatter(Logger logger, TextHandles handles) {
        this.logger = logger;
        handle = handles.getCharDigitSuffixFormatter();
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
    public Output format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        if(number == null) {
            number = BigDecimal.ZERO;
        }
        int interval = configuration.get(INTERVAL);
        validateConfiguration(interval);
        int shift = 0;
        int exponent = BigDecimalMath.exponent(number);
        boolean negativeExponent = exponent < 0;
        int index = exponent;
        if(interval != 1) {
            shift = Math.floorMod(exponent, interval);
            index = Math.floorDiv(exponent, interval);
            exponent = index * interval;
        }
        index = Math.abs(index);
        BigDecimal mantissa = BigDecimalMath.mantissa(number);
        if(shift != 0) {
            mantissa = mantissa.movePointRight(shift);
        }
        if(index == 0) {
            return new Output(mantissa, "", exponent);
        }
        StringBuilder sb = new StringBuilder();
        char[] characters = configuration.get(CHARACTERS);
        while(true) {
            sb.insert(0, characters[(index - 1) % characters.length]);
            if(index <= characters.length) {
                break;
            }
            index = (index - 1) / characters.length;
        }
        if(negativeExponent) {
            sb.insert(0, '-');
        } else if(configuration.get(EXPONENT_PLUS)) {
            sb.insert(0, '+');
        }
        return new Output(mantissa, sb.toString(), exponent);
    }

    /**
     * @return {@inheritDoc}. Always returns {@code true}
     */
    @Override
    public boolean isInfinite() {
        return true;
    }

    private void validateConfiguration(int interval) {
        if(interval <= 0) {
            throw LogUtils.logExceptionAndGet(logger,
                    "interval must be positive and non-zero",
                    TextConfigurationException::new);
        }
    }
}
