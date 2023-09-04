package me.datafox.dfxengine.math.numeral;

import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */
public final class LongNumeral extends AbstractNumeral {
    public LongNumeral(long number) {
        super(number);
    }

    @Override
    public NumeralType getType() {
        return NumeralType.LONG;
    }

    @Override
    public long longValue() throws ArithmeticException {
        return (long) number;
    }
}