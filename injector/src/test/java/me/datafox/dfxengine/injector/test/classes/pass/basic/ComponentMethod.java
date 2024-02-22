package me.datafox.dfxengine.injector.test.classes.pass.basic;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class ComponentMethod {
    @Component
    private NonComponent nonComponentClass() {
        return new NonComponent();
    }

    @Component
    private StaticNonComponent staticNonComponentClass() {
        return new StaticNonComponent();
    }
}
