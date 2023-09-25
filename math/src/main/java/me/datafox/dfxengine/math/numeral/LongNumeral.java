package me.datafox.dfxengine.math.numeral;

import lombok.EqualsAndHashCode;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * Implementation of {@link Numeral} backed with a {@code long}.
 *
 * @author datafox
 */

@EqualsAndHashCode(callSuper = false)
public final class LongNumeral extends AbstractNumeral {
    private final long number;
    private final NumeralType type;

    public LongNumeral(long number) {
        this.number = number;
        type = NumeralType.LONG;
    }

    /**
     * @return the {@link Number} backing this numeral
     */
    @Override
    public Number getNumber() {
        return number;
    }

    /**
     * @return the backing {@link Number}'s type ({@link NumeralType#LONG})
     */
    @Override
    public NumeralType getType() {
        return type;
    }

    /**
     * @return the backing {@code long} of this numeral
     */
    @Override
    public long longValue() {
        return number;
    }
}