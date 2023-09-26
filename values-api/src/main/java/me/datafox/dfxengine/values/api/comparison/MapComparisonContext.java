package me.datafox.dfxengine.values.api.comparison;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.math.api.Numeral;

/**
 * @author datafox
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString
public class MapComparisonContext extends ComparisonContext {
    @Builder.Default
    private final Numeral treatNonExistingAs = null;

    public Numeral getTreatNonExistingAs() {
        return treatNonExistingAs;
    }

    private static MapComparisonContext defaults = null;

    public static MapComparisonContext defaults() {
        if(defaults == null) {
            defaults = MapComparisonContext.builder().build();
        }

        return defaults;
    }
}
