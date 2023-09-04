package me.datafox.dfxengine.math.api.comparison;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.math.api.Numeral;

import java.util.Optional;

/**
 * @author datafox
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class MapComparisonContext extends ComparisonContext {
    @Builder.Default
    private final Optional<Numeral> treatNonExistingAs = Optional.empty();

    @Getter(lazy = true)
    private static final MapComparisonContext defaults = MapComparisonContext.builder().build();
}
