package me.datafox.dfxengine.values.api.comparison;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * Context used when doing {@link Comparison Comparisons} on {@link ValueMap ValueMaps}. In addition to the options in
 * {@link ComparisonContext}, {@link #treatNonExistingAs()} determines a {@link Numeral} value that is used as the
 * default input Numeral when a {@link Handle} key is requested that is not present in the map. A {@code null} value
 * represents that these Handles should be ignored instead. It is set to {@code null} by default.
 *
 * @author datafox
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString
public class MapComparisonContext extends ComparisonContext {
    @Builder.Default
    private final Numeral treatNonExistingAs = null;

    /**
     * Returns a {@link Numeral} to be used as an input if a {@link Handle} key is requested that is not present in the
     * {@link ValueMap}, or {@code null} if these handles should be ignored instead.
     *
     * @return {@link Numeral} to be used as an input if a {@link Handle} key is requested that is not present in the
     * {@link ValueMap}, or {@code null} if these handles should be ignored instead
     */
    public Numeral treatNonExistingAs() {
        return treatNonExistingAs;
    }

    private static MapComparisonContext defaults = null;

    /**
     * Returns a map comparison context with default values.
     *
     * @return Map comparison context with default values
     */
    public static MapComparisonContext defaults() {
        if(defaults == null) {
            defaults = MapComparisonContext.builder().build();
        }

        return defaults;
    }
}
