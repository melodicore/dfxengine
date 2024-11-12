package me.datafox.dfxengine.entities.api.condition;

import me.datafox.dfxengine.entities.api.component.Context;

import java.util.function.Predicate;

/**
 * @author datafox
 */
@FunctionalInterface
public interface Condition extends Predicate<Context> {
}
