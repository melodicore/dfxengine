package me.datafox.dfxengine.math.numeral;

import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */
public final class FloatNumeral extends AbstractNumeral {
    public FloatNumeral(float number) {
        super(number);
    }

    @Override
    public NumeralType getType() {
        return NumeralType.FLOAT;
    }

    @Override
    public float floatValue() throws ArithmeticException {
        return (float) number;
    }
}
