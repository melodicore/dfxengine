package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author datafox
 */

@Getter
@EqualsAndHashCode(callSuper = false)
public final class FloatNumeral extends AbstractNumeral {
    private final float number;
    private final NumeralType type = NumeralType.FLOAT;

    public FloatNumeral(float number) {
        if(!Float.isFinite(number)) {
            throw new IllegalArgumentException("infinite float value");
        }
        this.number = number;
    }

    @Override
    public Number getNumber() {
        return number;
    }

    @Override
    public float floatValue() {
        return number;
    }

    @Override
    public BigInteger bigIntValue() {
        return bigDecValue().toBigInteger();
    }

    @Override
    public double doubleValue() throws ArithmeticException {
        return Double.parseDouble(Float.toString(number));
    }

    @Override
    public BigDecimal bigDecValue() {
        return new BigDecimal(Float.toString(number));
    }
}
