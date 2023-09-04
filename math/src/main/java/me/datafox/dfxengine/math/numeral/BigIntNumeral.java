package me.datafox.dfxengine.math.numeral;

import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigInteger;

/**
 * @author datafox
 */
public final class BigIntNumeral extends AbstractNumeral {
    public BigIntNumeral(BigInteger number) {
        super(number);
    }

    @Override
    public NumeralType getType() {
        return NumeralType.BIG_INT;
    }

    @Override
    public BigInteger bigIntValue() {
        return (BigInteger) number;
    }
}