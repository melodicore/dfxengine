package me.datafox.dfxengine.values.api;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.math.api.Numeral;

import java.util.function.Function;

/**
 * A Modifier represents a math operation that can be attached to a {@link Value} or a {@link ValueMap}. When calling
 * {@link Value#getValue()}, the base numeral of that value will be processed through all modifiers associated with the
 * value, in the order specified by {@link #getPriority()}. The operation is specified by implementing
 * {@link #apply(Object)}.
 *
 * @author datafox
 */
public interface Modifier extends Dependency, Dependent, Function<Numeral, Numeral>, Comparable<Modifier> {
    /**
     * @return the priority of this modifier
     */
    int getPriority();

    /**
     * @param other the modifier to be compared
     * @return a negative integer, zero, or a positive integer as this modifier's priority is less than, equal to, or
     * greater than the specified modifier's priority
     */
    @Override
    default int compareTo(Modifier other) {
        return Integer.compare(getPriority(), other.getPriority());
    }
}
