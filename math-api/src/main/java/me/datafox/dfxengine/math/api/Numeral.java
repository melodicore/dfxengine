package me.datafox.dfxengine.math.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author datafox
 */
public interface Numeral extends Comparable<Numeral> {
    Number getNumber();

    NumeralType getType();

    Numeral convert(NumeralType type) throws ArithmeticException;

    Numeral convertIfAllowed(NumeralType type);

    Numeral convertToDecimal();

    boolean canConvert(NumeralType type);

    Numeral toSmallestType();

    int intValue() throws ArithmeticException;

    long longValue() throws ArithmeticException;

    BigInteger bigIntValue();

    float floatValue() throws ArithmeticException;

    double doubleValue() throws ArithmeticException;

    BigDecimal bigDecValue();
}
