package me.datafox.dfxengine.injector.test.classes.pass.per_instance;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class RequestingComponentMethodClass {
    @Component
    private NonComponentClass2 getNonComponentClass(PerInstanceComponentClass perInstanceComponent) {
        return new NonComponentClass2(perInstanceComponent);
    }
}
