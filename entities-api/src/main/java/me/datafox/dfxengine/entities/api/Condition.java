package me.datafox.dfxengine.entities.api;

import java.util.function.Predicate;

/**
 * @author datafox
 */
@FunctionalInterface
public interface Condition extends Predicate<Context> {
}
