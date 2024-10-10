package me.datafox.dfxengine.injector.test.classes.fail.parametric_event;

import me.datafox.dfxengine.injector.api.annotation.EventHandler;

import java.util.function.Predicate;

/**
 * @author datafox
 */
public class ParametricEventComponent {
    @EventHandler
    private void event(Predicate<?> predicate) {

    }
}
