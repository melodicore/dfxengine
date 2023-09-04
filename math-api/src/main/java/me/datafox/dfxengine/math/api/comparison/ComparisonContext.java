package me.datafox.dfxengine.math.api.comparison;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author datafox
 */
@Data
@SuperBuilder(toBuilder = true)
public class ComparisonContext {
    @Builder.Default
    private final boolean useModifiedValue = false;

    @Getter(lazy = true)
    private static final ComparisonContext defaults = ComparisonContext.builder().build();
}
