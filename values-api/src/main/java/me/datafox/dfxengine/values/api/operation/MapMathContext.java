package me.datafox.dfxengine.values.api.operation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * Context used when applying {@link Operation Operations} on {@link ValueMap ValueMaps}. In addition to the options in
 * {@link MathContext}, {@link #createNonExistingAs()} determines a {@link Numeral} value that is used as the default
 * input Numeral when a {@link Handle} key is requested that is not present in the map. A {@code null} value represents
 * that these Handles should be ignored instead. It is set to {@code null} by default.
 *
 * @author datafox
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString
public class MapMathContext extends MathContext {
    @Builder.Default
    private final Numeral createNonExistingAs = null;

    /**
     * Returns the {@link Numeral} to be used as an input if a {@link Handle} key is requested that is not present in
     * the {@link ValueMap}, or {@code null} if these Handles should be ignored instead
     *
     * @return {@link Numeral} to be used as an input if a {@link Handle} key is requested that is not present in the
     * {@link ValueMap}, or {@code null} if these Handles should be ignored instead
     */
    public Numeral createNonExistingAs() {
        return createNonExistingAs;
    }

    private static MapMathContext defaults = null;

    /**
     * Returns a map math context with default values.
     *
     * @return Map math context with default values
     */
    public static MapMathContext defaults() {
        if(defaults == null) {
            defaults = MapMathContext.builder().build();
        }

        return defaults;
    }
}
