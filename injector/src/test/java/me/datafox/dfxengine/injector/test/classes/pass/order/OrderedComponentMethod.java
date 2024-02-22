package me.datafox.dfxengine.injector.test.classes.pass.order;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class OrderedComponentMethod {
    @Component(order = -5)
    private Component2 getFirstComponent() {
        return new Component2();
    }
    @Component(order = 5)
    private Component2 getSecondComponent() {
        return new Component2();
    }
}
