package me.datafox.dfxengine.text.suffix;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.NumberSuffixFormatter;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.TextHandles;

import java.math.BigDecimal;

/**
 * A {@link NumberSuffixFormatter} that generates a traditional exponential notation suffix. Supports formatting in
 * scientific or engineering notation, or have any other interval that the exponents snaps to multiples of, configured
 * with {@link #INTERVAL}. The returned format has a lowercase e, and can be configured to output a plus on positive
 * exponents with {@link #EXPONENT_PLUS}.
 *
 * @author datafox
 */
@Getter
@Component(order = -1)
public class ExponentSuffixFormatter implements NumberSuffixFormatter {
    /**
     * Interval for formatted exponents. {@code 1} is equivalent to scientific notation and {@code 3} is equivalent to
     * engineering notation. The default value is {@code 1}.
     */
    public static final ConfigurationKey<Integer> INTERVAL = ConfigurationKey.of(1);

    /**
     * If {@code true}, will output a plus sign on positive exponents. The default value is {@code false}.
     */
    public static final ConfigurationKey<Boolean> EXPONENT_PLUS = ConfigurationKey.of(false);

    private final Handle handle;

    /**
     * @param handles {@link TextHandles} to be used for this formatter's {@link Handle}
     */
    @Inject
    public ExponentSuffixFormatter(TextHandles handles) {
        handle = handles.getExponentSuffixFormatter();
    }

    /**
     * @param number {@inheritDoc}
     * @param factory {@inheritDoc}
     * @param configuration {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Output format(BigDecimal number, TextFactory factory, TextConfiguration configuration) {
        int interval = configuration.get(INTERVAL);
        int shift = 0;
        int exponent = BigDecimalMath.exponent(number);
        if(interval != 1) {
            shift = Math.floorMod(exponent, interval);
            exponent = Math.floorDiv(exponent, interval) * interval;
        }
        BigDecimal mantissa = BigDecimalMath.mantissa(number);
        if(shift != 0) {
            mantissa = mantissa.movePointRight(shift);
        }
        String plus = "";
        if(configuration.get(EXPONENT_PLUS) && exponent >= 0) {
            plus = "+";
        }
        return new Output(mantissa, String.format("e%s%s", plus, exponent), exponent);
    }

    /**
     * @return {@inheritDoc}. Always returns {@code true}
     */
    @Override
    public boolean isInfinite() {
        return true;
    }
}
