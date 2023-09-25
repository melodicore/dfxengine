package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigInteger;

/**
 * Implementation of {@link Numeral} backed with a {@link BigInteger}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = false)
public final class BigIntNumeral extends AbstractNumeral {
    private final BigInteger number;
    private final NumeralType type;

    /**
     * @param number {@link BigInteger} to be associated with this numeral
     */
    public BigIntNumeral(BigInteger number) {
        this.number = number;
        type = NumeralType.BIG_INT;
    }

    /**
     * @param val {@link String} representation of the {@link BigInteger} to be associated with this numeral
     */
    public BigIntNumeral(String val) {
        this(new BigInteger(val));
    }

    /**
     * @param val {@code long} representation of the {@link BigInteger} to be associated with this numeral
     */
    public BigIntNumeral(long val) {
        this(BigInteger.valueOf(val));
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@link Number}'s type ({@link NumeralType#BIG_INT})
     */
    @Override
    public NumeralType getType() {
        return type;
    }

    /**
     * @return the backing {@link BigInteger} of this numeral
     */
    @Override
    public BigInteger bigIntValue() {
        return number;
    }
}