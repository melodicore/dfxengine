package me.datafox.dfxengine.values.api.comparison;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * Context used when doing {@link Comparison Comparisons} on {@link Value Values} and {@link ValueMap ValueMaps}.
 * If {@link #useModifiedValue()} is {@code true}, the result of {@link Value#getValue()} should be used for the
 * Comparison, otherwise the result of {@link Value#getBase()} should be used instead.
 *
 * @author datafox
 */
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@ToString
public class ComparisonContext {
    @Builder.Default
    private final boolean useModifiedValue = false;

    /**
     * @return {@code true} if the result of {@link Value#getValue()} should be used for the {@link Comparison}, or
     * {@code false} if the result of {@link Value#getBase()} should be used instead
     */
    public boolean useModifiedValue() {
        return useModifiedValue;
    }

    private static ComparisonContext defaults = null;

    /**
     * @return ComparisonContext with default values
     */
    public static ComparisonContext defaults() {
        if(defaults == null) {
            defaults = ComparisonContext.builder().build();
        }

        return defaults;
    }
}
