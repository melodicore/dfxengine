package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.internal.MathStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link Numeral} backed with a {@code double}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = true)
public final class DoubleNumeral extends AbstractNumeral {
    private static final Logger logger = LoggerFactory.getLogger(DoubleNumeral.class);

    private final double number;

    /**
     * @param number {@code double} to be associated with this numeral
     *
     * @throws IllegalArgumentException if the {@code double} is {@code NaN} or infinite.
     */
    public DoubleNumeral(double number) {
        super(NumeralType.DOUBLE);

        if(Double.isNaN(number)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.nanDoubleValue(),
                    IllegalArgumentException::new);
        }

        if(Double.isInfinite(number)) {
            throw LogUtils.logExceptionAndGet(logger,
                    MathStrings.infiniteDoubleValue(),
                    IllegalArgumentException::new);
        }

        this.number = number;
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@code double} of this numeral
     */
    @Override
    public double doubleValue() {
        return number;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
