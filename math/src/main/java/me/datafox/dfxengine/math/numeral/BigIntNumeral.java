package me.datafox.dfxengine.math.numeral;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigInteger;

/**
 * Implementation of {@link Numeral} backed with {@link BigInteger}.
 *
 * @author datafox
 */

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class BigIntNumeral extends AbstractNumeral {
    private final BigInteger number;
    private final NumeralType type = NumeralType.BIG_INT;

    public BigIntNumeral(String val) {
        this.number = new BigInteger(val);
    }

    public BigIntNumeral(long val) {
        this.number = BigInteger.valueOf(val);
    }

    @Override
    public BigInteger bigIntValue() {
        return number;
    }
}