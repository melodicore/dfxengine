package me.datafox.dfxengine.values.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.utils.Numerals;
import me.datafox.dfxengine.values.StaticValue;
import me.datafox.dfxengine.values.ValueImpl;
import me.datafox.dfxengine.values.api.Value;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Initializer methods for {@link Value Values} ({@link ValueImpl} and {@link StaticValue}).
 *
 * @author datafox
 */
public class Values {
    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param numeral {@link Numeral} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, Numeral numeral) {
        return new ValueImpl(handle, numeral);
    }

    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param i {@code int} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, int i) {
        return of(handle, Numerals.of(i));
    }

    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param l {@code long} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, long l) {
        return of(handle, Numerals.of(l));
    }

    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bi {@link BigInteger} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, BigInteger bi) {
        return of(handle, Numerals.of(bi));
    }

    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param f {@code float} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, float f) {
        return of(handle, Numerals.of(f));
    }

    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param d {@code double} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, double d) {
        return of(handle, Numerals.of(d));
    }

    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bd {@link BigDecimal} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, BigDecimal bd) {
        return of(handle, Numerals.of(bd));
    }

    /**
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param str {@link String} to initialize the {@link Value} with
     * @return {@link Value} with the specified parameters
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static Value of(Handle handle, String str) {
        return of(handle, Numerals.of(str));
    }

    /**
     * @param numeral {@link Numeral} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(Numeral numeral) {
        return new StaticValue(numeral);
    }

    /**
     * @param i {@code int} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(int i) {
        return of(Numerals.of(i));
    }

    /**
     * @param l {@code long} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(long l) {
        return of(Numerals.of(l));
    }

    /**
     * @param bi {@link BigInteger} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(BigInteger bi) {
        return of(Numerals.of(bi));
    }

    /**
     * @param f {@code float} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(float f) {
        return of(Numerals.of(f));
    }

    /**
     * @param d {@code double} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(double d) {
        return of(Numerals.of(d));
    }

    /**
     * @param bd {@link BigDecimal} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(BigDecimal bd) {
        return of(Numerals.of(bd));
    }

    /**
     * @param str {@link String} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static Value of(String str) {
        return of(Numerals.of(str));
    }
}
