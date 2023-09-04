package me.datafox.dfxengine.math.api.operation;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.math.api.NumeralType;

import java.util.Optional;

/**
 * @author datafox
 */
@Data
@SuperBuilder(toBuilder = true)
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class MathContext {
    @Builder.Default
    private final Optional<NumeralType> convertResult = Optional.empty();

    @Builder.Default
    private final boolean ignoreBadConversion = true;

    @Builder.Default
    private final boolean convertToDecimal = false;

    @Getter(lazy = true)
    private static final MathContext defaults = MathContext.builder().build();
}
