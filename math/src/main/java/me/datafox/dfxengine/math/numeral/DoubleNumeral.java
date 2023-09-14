package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */

@Getter
@EqualsAndHashCode(callSuper = false)
public final class DoubleNumeral extends AbstractNumeral {
    private final double number;
    private final NumeralType type = NumeralType.DOUBLE;

    public DoubleNumeral(double number) {
        if(!Double.isFinite(number)) {
            throw new IllegalArgumentException("infinite double value");
        }
        this.number = number;
    }

    @Override
    public Number getNumber() {
        return number;
    }

    @Override
    public double doubleValue() {
        return number;
    }
}
