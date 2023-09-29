package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

/**
 * @author datafox
 */
public interface SingleParameterOperation extends Operation {
    Numeral apply(Numeral source, Numeral parameter);

    @Override
    default int getParameterCount() {
        return 1;
    }

    @Override
    default Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters == null || parameters.length != 1) {
            throw new IllegalArgumentException("A SingleParameterOperation must be called with one parameter");
        }
        return apply(source, parameters[0]);
    }
}
