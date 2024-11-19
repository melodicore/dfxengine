package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

/**
 * An {@link Operation} that takes in two parameters along with an input.
 *
 * @author datafox
 */
public interface DualParameterOperation extends Operation {
    /**
     * Applies this operation to the specified source {@link Numeral} with the specified parameters.
     *
     * @param source source {@link Numeral} for this operation
     * @param parameter1 first parameter {@link Numeral} for this operation
     * @param parameter2 second parameter {@link Numeral} for this operation
     * @return resulting {@link Numeral} of this operation
     */
    Numeral apply(Numeral source, Numeral parameter1, Numeral parameter2);

    /**
     * Returns the amount of parameters this operation expects. Always returns {@code 2}.
     *
     * @return amount of parameters this operation expects
     */
    @Override
    default int getParameterCount() {
        return 2;
    }

    /**
     * Applies this operation to the specified source {@link Numeral} with the specified parameters. It is recommended
     * to use {@link #apply(Numeral, Numeral, Numeral)} where possible instead.
     *
     * @param source source {@link Numeral} for this operation
     * @param parameters parameter {@link Numeral Numerals} for this operation
     * @return resulting {@link Numeral} of this operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to {@link #getParameterCount()}
     */
    @Override
    default Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters == null || parameters.length != 2) {
            throw new IllegalArgumentException("A DualParameterOperation must be called with two parameters");
        }
        return apply(source, parameters[0], parameters[1]);
    }
}
