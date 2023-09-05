package me.datafox.dfxengine.math.numeral;

import lombok.Data;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.Conversion;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.math.utils.Range;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author datafox
 */
@Data
abstract class AbstractNumeral implements Numeral {
    protected final Number number;

    @Override
    public Numeral convert(NumeralType type) throws ArithmeticException {
        return Conversion.toNumeral(this, type);
    }

    @Override
    public Numeral convertIfAllowed(NumeralType type) {
        if(canConvert(type)) {
            return convert(type);
        }

        return this;
    }

    @Override
    public Numeral convertToDecimal() {
        return Conversion.toDecimal(this);
    }

    @Override
    public boolean canConvert(NumeralType type) {
        return Range.isOutOfRange(this, type);
    }

    @Override
    public Numeral toSmallestType() {
        return Conversion.toSmallestType(this);
    }

    @Override
    public int intValue() throws ArithmeticException {
        return Conversion.toInt(this);
    }

    @Override
    public long longValue() throws ArithmeticException {
        return Conversion.toLong(this);
    }

    @Override
    public BigInteger bigIntValue() {
        return Conversion.toBigInt(this);
    }

    @Override
    public float floatValue() throws ArithmeticException {
        return Conversion.toFloat(this);
    }

    @Override
    public double doubleValue() throws ArithmeticException {
        return Conversion.toDouble(this);
    }

    @Override
    public BigDecimal bigDecValue() {
        return Conversion.toBigDec(this);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Numeral)) {
            return false;
        }
        Numeral numeral = (Numeral) o;
        return Operations.compare(this, numeral) == 0;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)",
                getClass().getSimpleName().replace("Numeral", ""),
                number);
    }

    @Override
    public int compareTo(Numeral o) {
        return Operations.compare(this, o);
    }
}