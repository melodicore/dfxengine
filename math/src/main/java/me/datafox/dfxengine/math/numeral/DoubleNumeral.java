package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.internal.MathStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link Numeral} backed with a {@code double}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = false)
public final class DoubleNumeral extends AbstractNumeral {
    private final double number;
    private final NumeralType type;

    /**
     * @param number {@code double} to be associated with this numeral
     */
    public DoubleNumeral(double number) {
        if(!Double.isFinite(number)) {
            throw LogUtils.logExceptionAndGet(LoggerFactory.getLogger(getClass()),
                    MathStrings.infiniteDoubleValue(),
                    IllegalArgumentException::new);
        }

        this.number = number;
        type = NumeralType.DOUBLE;
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@link Number}'s type ({@link NumeralType#DOUBLE})
     */
    @Override
    public NumeralType getType() {
        return type;
    }

    /**
     * @return the backing {@code double} of this numeral
     */
    @Override
    public double doubleValue() {
        return number;
    }
}
