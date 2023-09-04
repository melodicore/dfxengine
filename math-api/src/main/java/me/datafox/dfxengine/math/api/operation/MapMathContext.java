package me.datafox.dfxengine.math.api.operation;

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
public class MapMathContext extends MathContext {
    @Builder.Default
    private final Optional<Numeral> createNonExisting = Optional.empty();

    @Getter(lazy = true)
    private static final MapMathContext defaults = MapMathContext.builder().build();
}
