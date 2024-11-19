package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link Numeral} backed with a {@code long}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = true)
public final class LongNumeral extends AbstractNumeral {
    private static final Logger logger = LoggerFactory.getLogger(LongNumeral.class);

    private final long number;

    /**
     * Public constructor for {@link LongNumeral}.
     *
     * @param number {@code long} to be associated with this numeral
     */
    public LongNumeral(long number) {
        super(NumeralType.LONG);
        this.number = number;
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
     * Returns the value of this numeral as a {@code long}.
     *
     * @return value of this numeral as a {@code long}
     */
    @Override
    public long longValue() {
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