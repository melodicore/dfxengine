package me.datafox.dfxengine.math.numeral;

import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */
public final class IntNumeral extends AbstractNumeral {
    public IntNumeral(int number) {
        super(number);
    }

    @Override
    public NumeralType getType() {
        return NumeralType.INT;
    }

    @Override
    public int intValue() throws ArithmeticException {
        return (int) number;
    }
}
