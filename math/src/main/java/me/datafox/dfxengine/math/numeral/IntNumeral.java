package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link Numeral} backed with an {@code int}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = true)
public final class IntNumeral extends AbstractNumeral {
    private static final Logger logger = LoggerFactory.getLogger(IntNumeral.class);

    private final int number;

    /**
     * Public constructor for {@link IntNumeral}.
     *
     * @param number {@code int} to be associated with this numeral
     */
    public IntNumeral(int number) {
        super(NumeralType.INT);
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
     * Returns the value of this numeral as an {@code int}.
     *
     * @return value of this numeral as an {@code int}
     */
    @Override
    public int intValue() {
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
