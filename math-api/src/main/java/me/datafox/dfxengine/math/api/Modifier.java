package me.datafox.dfxengine.math.api;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;

import java.util.function.Function;

/**
 * @author datafox
 */
public interface Modifier extends Dependency, Dependent, Function<Numeral, Numeral>, Comparable<Modifier> {
}
