package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * Implementation of {@link Numeral} backed with a {@link BigInteger}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = true)
public final class BigIntNumeral extends AbstractNumeral {
    private static final Logger logger = LoggerFactory.getLogger(BigIntNumeral.class);

    private final BigInteger number;

    /**
     * @param number {@link BigInteger} to be associated with this numeral
     */
    public BigIntNumeral(BigInteger number) {
        super(NumeralType.BIG_INT);
        this.number = number;
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
     * @return the backing {@link BigInteger} of this numeral
     */
    @Override
    public BigInteger bigIntValue() {
        return number;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}