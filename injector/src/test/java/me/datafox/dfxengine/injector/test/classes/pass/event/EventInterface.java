package me.datafox.dfxengine.injector.test.classes.pass.event;

import me.datafox.dfxengine.injector.api.annotation.EventHandler;

/**
 * @author datafox
 */
public interface EventInterface {
    @EventHandler
    void eventThing(Number number);
}
