package me.datafox.dfxengine.injector.api.annotation;

import me.datafox.dfxengine.injector.api.Injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An event handler is a method that is called automatically by the {@link Injector} when
 * {@link Injector#invokeEvent(Object)} is called with an object that is valid as the event handler method's parameter.
 * If the method returns an object, that object will be treated as another event.
 *
 * @author datafox
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventHandler {
}
