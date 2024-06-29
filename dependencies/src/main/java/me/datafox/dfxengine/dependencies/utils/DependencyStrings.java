package me.datafox.dfxengine.dependencies.utils;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;

/**
 * Contains all string literals used for logging in this module.
 *
 * @author datafox
 */
@SuppressWarnings("MissingJavadoc")
public class DependencyStrings {
    private static final String CYCLIC_DETECTED =
            "Dependency %s cannot be added to Dependent %s, cyclic dependency chain detected";

    public static String cyclicDetected(Dependency dependency, Dependent dependent) {
        return String.format(CYCLIC_DETECTED, dependency, dependent);
    }
}
