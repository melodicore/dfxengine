package me.datafox.dfxengine.injector.test.classes.pass.event;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.EventHandler;

import java.util.function.Predicate;

/**
 * @author datafox
 */
@Component
public class EventHandlerComponent extends EventHandlerSuper implements EventInterface {
    public int events = 0;
    public int interfaceEvents = 0;

    @EventHandler
    void event1(String str) {
        events++;
    }

    @EventHandler
    void event2(int integer) {
        events++;
    }

    @EventHandler
    String event3(Predicate<? super String> predicate) {
        events++;
        return "";
    }

    @Override
    public void eventThing(Number number) {
        interfaceEvents++;
    }
}
