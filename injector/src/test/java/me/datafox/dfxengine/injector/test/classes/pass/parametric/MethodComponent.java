package me.datafox.dfxengine.injector.test.classes.pass.parametric;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class MethodComponent {
    @Component
    private Parametric<Number,StringBuilder> getParametricClass() {
        return new Parametric<>();
    }
}
