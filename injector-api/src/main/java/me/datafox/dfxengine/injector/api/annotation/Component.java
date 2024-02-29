package me.datafox.dfxengine.injector.api.annotation;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * <p>
 * A component is a class that can be injected as a dependency. The Injector looks for all classes and methods annotated
 * as a component. A class will be registered as an injectable dependency, and a method's returned value will be used as
 * one. A class may have either a default constructor or a single constructor annotated with {@link Inject} with
 * dependencies as parameters. It may also have non-static and non-final dependency fields annotated with {@link Inject}
 * that are injected right after instantiation. Likewise, a component method may have other dependencies as parameters.
 * </p>
 * <p>
 * {@link #value()} determines how the component is instantiated. {@link InstantiationPolicy#ONCE} creates a singleton
 * instance of the component class at build time, while {@link InstantiationPolicy#PER_INSTANCE} instantiates a new
 * instance of the component class for every component that depends on it.
 * </p>
 * <p>
 * {@link #order()} determines the priority of components. If a single component is requested but multiple are present,
 * the highest priority component will be used. Only if multiple components with the same priority exist, an exception
 * will be thrown. This also affects the order of components in the {@link List} when multiple components are requested.
 * </p>
 *
 * @author datafox
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Component {
    /**
     * {@link InstantiationPolicy#ONCE} means that this component will be instantiated once.
     * {@link InstantiationPolicy#PER_INSTANCE} means that this component will be instantiated for every other component
     * that requests it.
     *
     * @return the associated {@link InstantiationPolicy}
     */
    InstantiationPolicy value() default InstantiationPolicy.ONCE;

    /**
     * The order affects both the order of components in the list when multiple components with this component's
     * signature are requested, and the priority when a single component with this component's signature has been
     * requested and multiple components with the same signature are present. Lower number means higher priority.
     *
     * @return {@code int} used for ordering components
     */
    int order() default 0;
}
