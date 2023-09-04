package me.datafox.dfxengine.math.numeral;

import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigDecimal;

/**
 * @author datafox
 */
public final class BigDecNumeral extends AbstractNumeral {
    public BigDecNumeral(BigDecimal number) {
        super(number);
    }

    @Override
    public NumeralType getType() {
        return NumeralType.BIG_DEC;
    }

    @Override
    public BigDecimal bigDecValue() {
        return (BigDecimal) number;
    }
}
