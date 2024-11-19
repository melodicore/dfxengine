package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

/**
 * An {@link Operation} that takes in no parameters, only an input.
 *
 * @author datafox
 */
public interface SourceOperation extends Operation {
    /**
     * Applies this operation to the specified source {@link Numeral}.
     *
     * @param source source {@link Numeral} for this operation
     * @return resulting {@link Numeral} of this operation
     */
    Numeral apply(Numeral source);

    /**
     * Returns the amount of parameters this operation expects. Always returns {@code 0}.
     *
     * @return amount of parameters this operation expects
     */
    @Override
    default int getParameterCount() {
        return 0;
    }

    /**
     * Applies this operation to the specified source {@link Numeral}. It is recommended to use {@link #apply(Numeral)}
     * where possible instead.
     *
     * @param source source {@link Numeral} for this operation
     * @param parameters parameter {@link Numeral Numerals} for this operation
     * @return resulting {@link Numeral} of this operation
     *
     * @throws IllegalArgumentException if the amount of parameters is not equal to {@link #getParameterCount()}
     */
    @Override
    default Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters != null && parameters.length != 0) {
            throw new IllegalArgumentException("A SourceOperation must be called with no parameters");
        }
        return apply(source);
    }
}
