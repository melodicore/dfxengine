package me.datafox.dfxengine.math.numeral;

import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */
public final class DoubleNumeral extends AbstractNumeral {
    public DoubleNumeral(double number) {
        super(number);
    }

    @Override
    public NumeralType getType() {
        return NumeralType.DOUBLE;
    }

    @Override
    public double doubleValue() throws ArithmeticException {
        return (double) number;
    }
}
