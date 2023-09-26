package me.datafox.dfxengine.values.api.operation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.math.api.Numeral;

/**
 * @author datafox
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString
public class MapMathContext extends MathContext {
    @Builder.Default
    private final Numeral createNonExistingAs = null;

    public Numeral createNonExistingAs() {
        return createNonExistingAs;
    }

    private static MapMathContext defaults = null;

    public static MapMathContext defaults() {
        if(defaults == null) {
            defaults = MapMathContext.builder().build();
        }

        return defaults;
    }
}
