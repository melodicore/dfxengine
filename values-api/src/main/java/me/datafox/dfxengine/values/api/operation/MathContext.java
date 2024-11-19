package me.datafox.dfxengine.values.api.operation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.math.api.NumeralType;
import me.datafox.dfxengine.math.api.exception.ExtendedArithmeticException;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

/**
 * Context used when applying {@link Operation Operations} on {@link Value Values} and {@link ValueMap ValueMaps}.
 * {@link #convertResultTo()} determines the {@link NumeralType} that the resulting {@link Numeral} should be converted
 * to after the operation. A {@code null} value represents no conversion. It is set to {@code null} by default. If
 * {@link #ignoreBadConversion()} is {@code true} and the conversion could not be done due to the resulting Numeral
 * being too large for that type, no conversion is done. Otherwise, an {@link ExtendedArithmeticException} is thrown.
 * It is set to {@code true} by default. If {@link #convertToDecimal()} is {@code true}, the input and parameter
 * Numerals are converted to the smallest possible decimal type before the operation. It is set to {@code false} by
 * default.
 *
 * @author datafox
 */
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@ToString
public class MathContext {
    @Builder.Default
    private final NumeralType convertResultTo = null;

    @Builder.Default
    private final boolean ignoreBadConversion = true;

    @Builder.Default
    private final boolean convertToDecimal = false;

    /**
     * Returns the {@link NumeralType} that the resulting {@link Numeral} from this operation should be converted to, or
     * {@code null} if no conversion should be done.
     *
     * @return {@link NumeralType} that the resulting {@link Numeral} from this operation should be converted to, or
     * {@code null} if no conversion should be done
     */
    public NumeralType convertResultTo() {
        return convertResultTo;
    }

    /**
     * Returns {@code true} if the conversion determined by {@link #convertResultTo()} should be ignored when the
     * resulting {@link Numeral} from this operation if the conversion cannot be done, or {@code false} if an
     * {@link ExtendedArithmeticException} should be thrown instead.
     *
     * @return {@code true} if the conversion determined by {@link #convertResultTo()} should be ignored when the
     * resulting {@link Numeral} from this operation if the conversion cannot be done, or {@code false} if an
     * {@link ExtendedArithmeticException} should be thrown instead.
     *
     */
    public boolean ignoreBadConversion() {
        return ignoreBadConversion;
    }

    /**
     * Returns {@code true} if the input and parameter {@link Numeral Numerals} should be converted to a decimal type
     * before the operation, or {@code false} otherwise.
     * @return {@code true} if the input and parameter {@link Numeral Numerals} should be converted to a decimal type
     * before the operation, or {@code false} otherwise.
     */
    public boolean convertToDecimal() {
        return convertToDecimal;
    }

    private static MathContext defaults = null;

    /**
     * Returns a math context with default values.
     *
     * @return Math context with default values
     */
    public static MathContext defaults() {
        if(defaults == null) {
            defaults = MathContext.builder().build();
        }

        return defaults;
    }
}
