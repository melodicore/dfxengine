package me.datafox.dfxengine.injector.test.classes.pass.basic;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class ComponentMethodClass {
    @Component
    private NonComponentClass nonComponentClass() {
        return new NonComponentClass();
    }
}
