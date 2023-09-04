package me.datafox.dfxengine.math.numeral;

import lombok.Data;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.ConversionUtils;
import me.datafox.dfxengine.math.utils.OperationUtils;
import me.datafox.dfxengine.math.utils.RangeUtils;

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
        return ConversionUtils.toNumeral(this, type);
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
        return ConversionUtils.toDecimal(this);
    }

    @Override
    public boolean canConvert(NumeralType type) {
        return RangeUtils.isOutOfRange(this, type);
    }

    @Override
    public Numeral toSmallestType() {
        return ConversionUtils.toSmallestType(this);
    }

    @Override
    public int intValue() throws ArithmeticException {
        return ConversionUtils.toInt(this);
    }

    @Override
    public long longValue() throws ArithmeticException {
        return ConversionUtils.toLong(this);
    }

    @Override
    public BigInteger bigIntValue() {
        return ConversionUtils.toBigInt(this);
    }

    @Override
    public float floatValue() throws ArithmeticException {
        return ConversionUtils.toFloat(this);
    }

    @Override
    public double doubleValue() throws ArithmeticException {
        return ConversionUtils.toDouble(this);
    }

    @Override
    public BigDecimal bigDecValue() {
        return ConversionUtils.toBigDec(this);
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
        return OperationUtils.compare(this, numeral) == 0;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)",
                getClass().getSimpleName().replace("Numeral", ""),
                number);
    }

    @Override
    public int compareTo(Numeral o) {
        return OperationUtils.compare(this, o);
    }
}
