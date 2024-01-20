package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;
import me.datafox.dfxengine.values.api.Value;
import me.datafox.dfxengine.values.api.ValueMap;

import java.util.function.BiFunction;

/**
 * An Operation represents a math operation that has one input and an arbitrary number of parameters. When used with
 * {@link Value#apply(Operation, MathContext, Numeral...)} or any of the other apply methods in Value and
 * {@link ValueMap}, the Value is used as the input.
 *
 * @author datafox
 */
public interface Operation extends BiFunction<Numeral, Numeral[], Numeral> {
    /**
     * @return the amount of parameters this operation expects
     */
    int getParameterCount();

    /**
     * @param source source {@link Numeral} for this operation
     * @param parameters parameter {@link Numeral Numerals} for this operation
     * @return resulting {@link Numeral} of this operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to {@link #getParameterCount()}
     */
    @Override
    Numeral apply(Numeral source, Numeral ... parameters);
}
