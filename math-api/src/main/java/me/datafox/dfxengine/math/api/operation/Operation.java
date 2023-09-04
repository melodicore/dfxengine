package me.datafox.dfxengine.math.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @author datafox
 */
public interface Operation extends BiFunction<Numeral, List<Numeral>, Numeral> {
    int getParameterCount();

    @Override
    Numeral apply(Numeral source, List<Numeral> parameters) throws IllegalArgumentException;
}
