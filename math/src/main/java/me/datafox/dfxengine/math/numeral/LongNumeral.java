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
     * @param number {@code long} to be associated with this numeral
     */
    public LongNumeral(long number) {
        super(NumeralType.LONG);
        this.number = number;
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@code long} of this numeral
     */
    @Override
    public long longValue() {
        return number;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}