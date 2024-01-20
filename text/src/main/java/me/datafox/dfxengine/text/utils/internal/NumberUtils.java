package me.datafox.dfxengine.text.utils.internal;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author datafox
 */
public class NumberUtils {
    public static BigDecimal toBigDecimal(Number number) {
        if(number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else if(number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        } else if(number instanceof Integer || number instanceof Long) {
            return new BigDecimal(number.longValue());
        } else {
            return new BigDecimal(number.toString());
        }
    }

    public static int getRandom(int max) {
        return (int) (Math.random() * max);
    }
}
