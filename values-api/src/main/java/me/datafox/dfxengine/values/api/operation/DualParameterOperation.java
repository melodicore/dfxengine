package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

import java.util.List;

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
    default Numeral apply(Numeral source, List<Numeral> parameters) {
        if(parameters == null || parameters.size() != getParameterCount()) {
            throw new IllegalArgumentException("A SingleParameterOperation must be called with a List of one parameter");
        }
        return apply(source, parameters.get(0), parameters.get(1));
    }
}
