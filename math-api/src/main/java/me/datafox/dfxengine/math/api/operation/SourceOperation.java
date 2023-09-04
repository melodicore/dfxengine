package me.datafox.dfxengine.math.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

import java.util.List;

/**
 * @author datafox
 */
public interface SourceOperation extends Operation {
    Numeral apply(Numeral source);

    @Override
    default int getParameterCount() {
        return 0;
    }

    @Override
    default Numeral apply(Numeral source, List<Numeral> parameters) {
        if(parameters != null && !parameters.isEmpty()) {
            throw new IllegalArgumentException("A SourceOperation must be called with an empty List of parameters");
        }
        return apply(source);
    }
}
