package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * Implementation of {@link Numeral} backed with an {@code int}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = true)
public final class IntNumeral extends AbstractNumeral {
    private final int number;

    /**
     * @param number {@code int} to be associated with this numeral
     */
    public IntNumeral(int number) {
        super(NumeralType.INT);
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
     * @return the backing {@code int} of this numeral
     */
    @Override
    public int intValue() {
        return number;
    }
}
