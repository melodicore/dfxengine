package me.datafox.dfxengine.injector.test.classes.pass.parametric;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class MethodComponentClass {
    @Component
    private ParametricClass<Number,StringBuilder> getParametricClass() {
        return new ParametricClass<>();
    }
}
