package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

/**
 * @author datafox
 */
public interface DualParameterOperation extends Operation {
    Numeral apply(Numeral source, Numeral parameter1, Numeral parameter2);

    @Override
    default int getParameterCount() {
        return 2;
    }

    @Override
    default Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters == null || parameters.length != 2) {
            throw new IllegalArgumentException("A DualParameterOperation must be called with two parameters");
        }
        return apply(source, parameters[0], parameters[1]);
    }
}
