package me.datafox.dfxengine.math.numeral;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.math.api.NumeralType;

import java.math.BigDecimal;

/**
 * @author datafox
 */

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class BigDecNumeral extends AbstractNumeral {
    private final BigDecimal number;
    private final NumeralType type = NumeralType.BIG_DEC;

    public BigDecNumeral(String val) {
        this.number = new BigDecimal(val);
    }

    @Override
    public BigDecimal bigDecValue() {
        return number;
    }
}

