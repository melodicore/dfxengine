package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

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
    default Numeral apply(Numeral source, Numeral ... parameters) {
        if(parameters != null && parameters.length != 0) {
            throw new IllegalArgumentException("A SourceOperation must be called with no parameters");
        }
        return apply(source);
    }
}
