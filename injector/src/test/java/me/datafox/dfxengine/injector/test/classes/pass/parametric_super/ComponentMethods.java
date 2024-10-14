package me.datafox.dfxengine.injector.test.classes.pass.parametric_super;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class ComponentMethods {
    @Component
    public static ParametricClass<Interface1, Interface3> i13() {
        return new ParametricClass<>("i13");
    }

    @Component
    public static ParametricClass<Interface4, Interface2> i42() {
        return new ParametricClass<>("i42");
    }

    @Component
    public static ParametricClass<? super Interface4, Interface5> is45() {
        return new ParametricClass<>("is45");
    }
}
