package me.datafox.dfxengine.injector.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that a constructor or a field is eligible for dependency injection. When annotated on a constructor, all that
 * constructor's parameters will be treated as dependencies. There may only be a single constructor annotated to be
 * injected. In the case of a field, it will be treated as a dependency and initialized during instantiation of the
 * class. Dependencies will only be injected if the class is a {@link Component} or if it is otherwise instantiated with
 * the Injector.
 *
 * @author datafox
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface Inject {
}
