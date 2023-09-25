package me.datafox.dfxengine.injector.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An initialization method is a method that is invoked after its class has been instantiated by the Injector. If the
 * class is a {@link Component}, the method is only invoked after all Components are instantiated. This is especially
 * useful when you must have cyclic dependencies. The initialization method may have parameters which are treated as
 * dependencies. A class may have multiple initialization methods, but there are no guarantees on the invocation order.
 *
 * @author datafox
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Initialize {
}
