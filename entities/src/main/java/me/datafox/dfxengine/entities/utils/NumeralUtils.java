package me.datafox.dfxengine.entities.utils;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.utils.Numerals;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author datafox
 */
public class NumeralUtils {
    public static Numeral getNumeral(NumeralType type, String value) {
        switch(type) {
            case INT:
                return Numerals.of(Integer.parseInt(value));
            case LONG:
                return Numerals.of(Long.parseLong(value));
            case BIG_INT:
                return Numerals.of(new BigInteger(value));
            case FLOAT:
                return Numerals.of(Float.parseFloat(value));
            case DOUBLE:
                return Numerals.of(Double.parseDouble(value));
            case BIG_DEC:
                return Numerals.of(new BigDecimal(value));
            default:
                return Numerals.of(value);
        }
    }
}
