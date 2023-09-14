package me.datafox.dfxengine.math.numeral;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class LongNumeral extends AbstractNumeral {
    private final long number;
    private final NumeralType type = NumeralType.LONG;

    @Override
    public Number getNumber() {
        return number;
    }

    @Override
    public long longValue() {
        return number;
    }
}