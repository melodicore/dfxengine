package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigDecimal;

/**
 * Implementation of {@link Numeral} backed with a {@link BigDecimal}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = false)
public final class BigDecNumeral extends AbstractNumeral {
    private final BigDecimal number;
    private final NumeralType type;

    /**
     * @param number {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(BigDecimal number) {
        this.number = number;
        type = NumeralType.BIG_DEC;
    }

    /**
     * @param val {@link String} representation of the {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(String val) {
        this(new BigDecimal(val));
    }

    /**
     * @param val {@code long} representation of the {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(long val) {
        this(BigDecimal.valueOf(val));
    }

    /**
     * @param val {@code double} representation of the {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(double val) {
        this(BigDecimal.valueOf(val));
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@link Number}'s type ({@link NumeralType#BIG_DEC})
     */
    @Override
    public NumeralType getType() {
        return type;
    }

    /**
     * @return the backing {@link BigDecimal} of this numeral
     */
    @Override
    public BigDecimal bigDecValue() {
        return number;
    }
}

