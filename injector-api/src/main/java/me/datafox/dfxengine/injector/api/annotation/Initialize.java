package me.datafox.dfxengine.injector.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An initialization method is a method that is invoked after all {@link Component Components} are instantiated. It is
 * only recognised if its declaring class is a component. This is especially useful when you must have cyclic
 * dependencies. The initialization method may have parameters which are treated as dependencies. A class may have
 * multiple initialization methods, and the order of all initialization methods can be determined by setting
 * {@link #value()}, where lower values mean earlier invocation.
 *
 * @author datafox
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Initialize {
    /**
     * Used for determining the invocation order of all initialization methods. Lower number means earlier invocation.
     *
     * @return {@code int} used for ordering initialization methods
     */
    int value() default 0;
}
