package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Implementation of {@link Numeral} backed with a {@link BigDecimal}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = true)
public final class BigDecNumeral extends AbstractNumeral {
    private static final Logger logger = LoggerFactory.getLogger(BigDecNumeral.class);

    private final BigDecimal number;

    /**
     * Public constructor for {@link BigDecNumeral}.
     *
     * @param number {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(BigDecimal number) {
        super(NumeralType.BIG_DEC);
        this.number = number;
    }

    /**
     * Public constructor for {@link BigDecNumeral}.
     *
     * @param val {@link String} representation of the {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(String val) {
        this(new BigDecimal(val));
    }

    /**
     * Public constructor for {@link BigDecNumeral}.
     *
     * @param val {@code long} representation of the {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(long val) {
        this(BigDecimal.valueOf(val));
    }

    /**
     * Public constructor for {@link BigDecNumeral}.
     *
     * @param val {@code double} representation of the {@link BigDecimal} to be associated with this numeral
     */
    public BigDecNumeral(double val) {
        this(BigDecimal.valueOf(val));
    }

    /**
     * Returns the {@link Number} backing this numeral.
     *
     * @return {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * Returns the value of this numeral as a {@link BigDecimal}.
     *
     * @return value of this numeral as a {@link BigDecimal}
     */
    @Override
    public BigDecimal bigDecValue() {
        return number;
    }

    /**
     * Returns the {@link Logger} for this numeral.
     *
     * @return {@link Logger} for this numeral
     */
    @Override
    protected Logger getLogger() {
        return logger;
    }
}

