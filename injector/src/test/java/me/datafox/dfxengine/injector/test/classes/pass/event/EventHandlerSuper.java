package me.datafox.dfxengine.injector.test.classes.pass.event;

import me.datafox.dfxengine.injector.api.annotation.EventHandler;

/**
 * @author datafox
 */
public class EventHandlerSuper {
    public int superEvents = 0;

    @EventHandler
    private void event(double d) {
        superEvents++;
    }
}
