package me.datafox.dfxengine.math.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

import java.util.List;

/**
 * @author datafox
 */
public interface VarargParameterOperation extends Operation {
    Numeral apply(Numeral source, Numeral ... parameters);

    @Override
    default Numeral apply(Numeral source, List<Numeral> parameters) {
        if(parameters == null || parameters.size() != getParameterCount()) {
            throw new IllegalArgumentException(String.format(
                    "This VarargParameterOperation must be called with a parameter List of size %s",
                    getParameterCount()));
        }
        return apply(source, parameters.toArray(Numeral[]::new));
    }
}
