package me.datafox.dfxengine.values.api.operation;

import me.datafox.dfxengine.math.api.Numeral;

import java.util.function.BiFunction;

/**
 * @author datafox
 */
public interface Operation extends BiFunction<Numeral, Numeral[], Numeral> {
    int getParameterCount();

    @Override
    Numeral apply(Numeral source, Numeral ... parameters) throws IllegalArgumentException;
}
