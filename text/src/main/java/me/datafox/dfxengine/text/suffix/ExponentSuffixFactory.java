package me.datafox.dfxengine.text.suffix;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.text.api.ConfigurationKey;
import me.datafox.dfxengine.text.api.NumberSuffixFactory;
import me.datafox.dfxengine.text.api.TextConfiguration;
import me.datafox.dfxengine.text.api.TextFactory;
import me.datafox.dfxengine.text.utils.TextHandles;

import java.math.BigDecimal;

/**
 * @author datafox
 */
@Component
public class ExponentSuffixFactory implements NumberSuffixFactory {
    /**
     * Interval for formatted exponents. {@code 1} is equivalent to scientific, {@code 3} is equivalent to
     * engineering. The default value is {@code 1}.
     */
    public static final ConfigurationKey<Integer> INTERVAL = ConfigurationKey.of(1);

    @Getter
    private final Handle handle;

    @Inject
    public ExponentSuffixFactory(TextHandles handles) {
        handle = handles.getExponentSuffixFactory();
    }

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
        return new Output(mantissa, String.format("e%s", exponent), exponent);
    }

    @Override
    public boolean isInfinite() {
        return true;
    }
}
