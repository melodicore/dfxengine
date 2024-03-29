package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

/**
 * An {@link Operation} that takes in one parameter along with an input.
 *
 * @author datafox
 */
public interface SingleParameterOperation extends Operation {
    /**
     * @param source source {@link Numeral} for this operation
     * @param parameter parameter {@link Numeral} for this operation
     * @return resulting {@link Numeral} of this operation
     */
    Numeral apply(Numeral source, Numeral parameter);

    /**
     * @return the amount of parameters this operation expects (1)
     */
    @Override
    default int getParameterCount() {
        return 1;
    }

    /**
     * It is recommended to use {@link #apply(Numeral, Numeral)} where possible instead.
     *
     * @param source source {@link Numeral} for this operation
     * @param parameters parameter {@link Numeral Numerals} for this operation
     * @return resulting {@link Numeral} of this operation
     * @throws IllegalArgumentException if the amount of parameters is not equal to {@link #getParameterCount()} (1)
     */
    @Override
    default Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters == null || parameters.length != 1) {
            throw new IllegalArgumentException("A SingleParameterOperation must be called with one parameter");
        }
        return apply(source, parameters[0]);
    }
}
