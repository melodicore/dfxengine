package me.datafox.dfxengine.values.api.comparison;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author datafox
 */
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@ToString
public class ComparisonContext {
    @Builder.Default
    private final boolean useModifiedValue = false;

    public boolean useModifiedValue() {
        return useModifiedValue;
    }

    private static ComparisonContext defaults = null;

    public static ComparisonContext defaults() {
        if(defaults == null) {
            defaults = ComparisonContext.builder().build();
        }

        return defaults;
    }
}
