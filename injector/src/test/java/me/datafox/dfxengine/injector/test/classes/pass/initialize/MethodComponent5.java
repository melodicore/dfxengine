package me.datafox.dfxengine.injector.test.classes.pass.initialize;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class MethodComponent5 {
    @Component
    ComponentWithInitialize another() {
        return new ComponentWithInitialize();
    }
}
