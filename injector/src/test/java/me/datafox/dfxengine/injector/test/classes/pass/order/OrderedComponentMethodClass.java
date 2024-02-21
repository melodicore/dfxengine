package me.datafox.dfxengine.injector.test.classes.pass.order;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class OrderedComponentMethodClass {
    @Component(order = -5)
    private ComponentClass2 getFirstComponent() {
        return new ComponentClass2();
    }
    @Component(order = 5)
    private ComponentClass2 getSecondComponent() {
        return new ComponentClass2();
    }
}
