package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

import java.util.List;

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
    default Numeral apply(Numeral source, List<Numeral> parameters) {
        if(parameters == null || parameters.size() != getParameterCount()) {
            throw new IllegalArgumentException("A SingleParameterOperation must be called with a List of one parameter");
        }
        return apply(source, parameters.get(1));
    }
}
