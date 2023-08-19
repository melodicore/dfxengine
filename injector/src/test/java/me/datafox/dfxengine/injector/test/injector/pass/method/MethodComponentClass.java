package me.datafox.dfxengine.injector.test.injector.pass.method;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class MethodComponentClass {
    @Component
    private NonComponentClass buildNonComponentClass() {
        return new NonComponentClass();
    }

    @Component(InstantiationPolicy.PER_INSTANCE)
    private static NonComponentClass staticBuildNonComponentClass() {
        return new NonComponentClass();
    }
}
