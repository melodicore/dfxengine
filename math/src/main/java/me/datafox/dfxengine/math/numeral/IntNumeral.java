package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * Implementation of {@link Numeral} backed with an {@code int}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = false)
public final class IntNumeral extends AbstractNumeral {
    private final int number;
    private final NumeralType type;

    /**
     * @param number {@code int} to be associated with this numeral
     */
    public IntNumeral(int number) {
        this.number = number;
        type = NumeralType.INT;
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@link Number}'s type ({@link NumeralType#INT})
     */
    @Override
    public NumeralType getType() {
        return type;
    }

    /**
     * @return the backing {@code int} of this numeral
     */
    @Override
    public int intValue() {
        return number;
    }
}
