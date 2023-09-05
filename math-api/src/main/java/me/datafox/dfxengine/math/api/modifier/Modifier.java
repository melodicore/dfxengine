package me.datafox.dfxengine.math.api.modifier;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.math.api.Numeral;

import java.util.function.Function;

/**
 * @author datafox
 */
public interface Modifier extends Dependency, Dependent, Function<Numeral, Numeral>, Comparable<Modifier> {
    int getPriority();

    @Override
    default int compareTo(Modifier o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
