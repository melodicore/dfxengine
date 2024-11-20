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
     * Returns a {@link Value} with the specified {@link Numeral}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param numeral {@link Numeral} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, Numeral numeral, boolean immutable) {
        return new ValueImpl(handle, numeral, immutable);
    }

    /**
     * Returns a {@link Value} with a {@link Numeral} backed with the specified {@code int}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param i {@code int} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, int i, boolean immutable) {
        return of(handle, Numerals.of(i), immutable);
    }

    /**
     * Returns a {@link Value} with a {@link Numeral} backed with the specified {@code long}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param l {@code long} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, long l, boolean immutable) {
        return of(handle, Numerals.of(l), immutable);
    }

    /**
     * Returns a {@link Value} with a {@link Numeral} backed with the specified {@link BigInteger}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bi {@link BigInteger} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, BigInteger bi, boolean immutable) {
        return of(handle, Numerals.of(bi), immutable);
    }

    /**
     * Returns a {@link Value} with a {@link Numeral} backed with the specified {@code float}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param f {@code float} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, float f, boolean immutable) {
        return of(handle, Numerals.of(f), immutable);
    }

    /**
     * Returns a {@link Value} with a {@link Numeral} backed with the specified {@code double}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param d {@code double} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, double d, boolean immutable) {
        return of(handle, Numerals.of(d), immutable);
    }

    /**
     * Returns a {@link Value} with a {@link Numeral} backed with the specified {@link BigDecimal}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bd {@link BigDecimal} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     */
    public static Value of(Handle handle, BigDecimal bd, boolean immutable) {
        return of(handle, Numerals.of(bd), immutable);
    }

    /**
     * Returns a {@link Value} with a {@link Numeral} backed with the specified {@link String}. Uses
     * {@link Numerals#of(String)} so the resulting numeral will be backed with a {@link BigInteger} or
     * {@link BigDecimal}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param str {@link String} to initialize the {@link Value} with
     * @param immutable {@code true} if the {@link Value} should be immutable
     * @return {@link Value} with the specified parameters
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static Value of(Handle handle, String str, boolean immutable) {
        return of(handle, Numerals.of(str), immutable);
    }

    /**
     * Returns a static {@link Value} with the specified {@link Numeral}.
     *
     * @param numeral {@link Numeral} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(Numeral numeral) {
        return new StaticValue(numeral);
    }

    /**
     * Returns a static {@link Value} with a {@link Numeral} backed with the specified {@code int}.
     *
     * @param i {@code int} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(int i) {
        return of(Numerals.of(i));
    }

    /**
     * Returns a static {@link Value} with a {@link Numeral} backed with the specified {@code long}.
     *
     * @param l {@code long} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(long l) {
        return of(Numerals.of(l));
    }

    /**
     * Returns a static {@link Value} with a {@link Numeral} backed with the specified {@link BigInteger}.
     *
     * @param bi {@link BigInteger} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(BigInteger bi) {
        return of(Numerals.of(bi));
    }

    /**
     * Returns a static {@link Value} with a {@link Numeral} backed with the specified {@code float}.
     *
     * @param f {@code float} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(float f) {
        return of(Numerals.of(f));
    }

    /**
     * Returns a static {@link Value} with a {@link Numeral} backed with the specified {@code double}.
     *
     * @param d {@code double} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(double d) {
        return of(Numerals.of(d));
    }

    /**
     * Returns a static {@link Value} with a {@link Numeral} backed with the specified {@link BigDecimal}.
     *
     * @param bd {@link BigDecimal} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     */
    public static Value of(BigDecimal bd) {
        return of(Numerals.of(bd));
    }

    /**
     * Returns a static {@link Value} with a {@link Numeral} backed with the specified {@link String}. Uses
     * {@link Numerals#of(String)} so the resulting numeral will be backed with a {@link BigInteger} or
     * {@link BigDecimal}.
     *
     * @param str {@link String} to initialize the {@link Value} with
     * @return static {@link Value} with the specified value
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static Value of(String str) {
        return of(Numerals.of(str));
    }

    /**
     * Returns a mutable {@link Value} with the specified {@link Numeral}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param numeral {@link Numeral} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     */
    public static Value mutable(Handle handle, Numeral numeral) {
        return of(handle, numeral, false);
    }

    /**
     * Returns a mutable {@link Value} with a {@link Numeral} backed with the specified {@code int}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param i {@code int} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     */
    public static Value mutable(Handle handle, int i) {
        return mutable(handle, Numerals.of(i));
    }

    /**
     * Returns a mutable {@link Value} with a {@link Numeral} backed with the specified {@code long}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param l {@code long} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     */
    public static Value mutable(Handle handle, long l) {
        return mutable(handle, Numerals.of(l));
    }

    /**
     * Returns a mutable {@link Value} with a {@link Numeral} backed with the specified {@link BigInteger}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bi {@link BigInteger} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     */
    public static Value mutable(Handle handle, BigInteger bi) {
        return mutable(handle, Numerals.of(bi));
    }

    /**
     * Returns a mutable {@link Value} with a {@link Numeral} backed with the specified {@code float}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param f {@code float} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     */
    public static Value mutable(Handle handle, float f) {
        return mutable(handle, Numerals.of(f));
    }

    /**
     * Returns a mutable {@link Value} with a {@link Numeral} backed with the specified {@code double}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param d {@code double} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     */
    public static Value mutable(Handle handle, double d) {
        return mutable(handle, Numerals.of(d));
    }

    /**
     * Returns a mutable {@link Value} with a {@link Numeral} backed with the specified {@link BigDecimal}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bd {@link BigDecimal} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     */
    public static Value mutable(Handle handle, BigDecimal bd) {
        return mutable(handle, Numerals.of(bd));
    }

    /**
     * Returns a mutable {@link Value} with a {@link Numeral} backed with the specified {@link String}. Uses
     * {@link Numerals#of(String)} so the resulting numeral will be backed with a {@link BigInteger} or
     * {@link BigDecimal}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param str {@link String} to initialize the {@link Value} with
     * @return mutable {@link Value} with the specified parameters
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static Value mutable(Handle handle, String str) {
        return mutable(handle, Numerals.of(str));
    }

    /**
     * Returns an immutable {@link Value} with the specified {@link Numeral}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param numeral {@link Numeral} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     */
    public static Value immutable(Handle handle, Numeral numeral) {
        return of(handle, numeral, true);
    }

    /**
     * Returns an immutable {@link Value} with a {@link Numeral} backed with the specified {@code int}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param i {@code int} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     */
    public static Value immutable(Handle handle, int i) {
        return immutable(handle, Numerals.of(i));
    }

    /**
     * Returns an immutable {@link Value} with a {@link Numeral} backed with the specified {@code long}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param l {@code long} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     */
    public static Value immutable(Handle handle, long l) {
        return immutable(handle, Numerals.of(l));
    }

    /**
     * Returns an immutable {@link Value} with a {@link Numeral} backed with the specified {@link BigInteger}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bi {@link BigInteger} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     */
    public static Value immutable(Handle handle, BigInteger bi) {
        return immutable(handle, Numerals.of(bi));
    }

    /**
     * Returns an immutable {@link Value} with a {@link Numeral} backed with the specified {@code float}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param f {@code float} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     */
    public static Value immutable(Handle handle, float f) {
        return immutable(handle, Numerals.of(f));
    }

    /**
     * Returns an immutable {@link Value} with a {@link Numeral} backed with the specified {@code double}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param d {@code double} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     */
    public static Value immutable(Handle handle, double d) {
        return immutable(handle, Numerals.of(d));
    }

    /**
     * Returns an immutable {@link Value} with a {@link Numeral} backed with the specified {@link BigDecimal}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param bd {@link BigDecimal} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     */
    public static Value immutable(Handle handle, BigDecimal bd) {
        return immutable(handle, Numerals.of(bd));
    }

    /**
     * Returns an immutable {@link Value} with a {@link Numeral} backed with the specified {@link String}. Uses
     * {@link Numerals#of(String)} so the resulting numeral will be backed with a {@link BigInteger} or
     * {@link BigDecimal}.
     *
     * @param handle {@link Handle} identifier for the {@link Value}
     * @param str {@link String} to initialize the {@link Value} with
     * @return immutable {@link Value} with the specified parameters
     *
     * @throws NumberFormatException if str is not a valid number representation
     */
    public static Value immutable(Handle handle, String str) {
        return immutable(handle, Numerals.of(str));
    }
}
