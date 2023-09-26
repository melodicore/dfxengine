package me.datafox.dfxengine.values.api.operation;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.math.api.NumeralType;

/**
 * @author datafox
 */
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@ToString
public class MathContext {
    @Builder.Default
    private final NumeralType conversionResult = null;

    @Builder.Default
    private final boolean ignoreBadConversion = true;

    @Builder.Default
    private final boolean convertToDecimal = false;

    public NumeralType conversionResult() {
        return conversionResult;
    }

    public boolean ignoreBadConversion() {
        return ignoreBadConversion;
    }

    public boolean convertToDecimal() {
        return convertToDecimal;
    }

    private static MathContext defaults = null;

    public static MathContext defaults() {
        if(defaults == null) {
            defaults = MathContext.builder().build();
        }

        return defaults;
    }
}
