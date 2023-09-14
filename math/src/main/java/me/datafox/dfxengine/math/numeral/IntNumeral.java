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
public final class IntNumeral extends AbstractNumeral {
    private final int number;
    private final NumeralType type = NumeralType.INT;

    @Override
    public Number getNumber() {
        return number;
    }

    @Override
    public int intValue() {
        return number;
    }
}
