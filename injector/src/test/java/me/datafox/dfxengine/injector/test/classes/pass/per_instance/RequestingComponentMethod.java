package me.datafox.dfxengine.injector.test.classes.pass.per_instance;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class RequestingComponentMethod {
    @Component
    private NonComponent2 getNonComponentClass(PerInstanceComponent perInstanceComponent) {
        return new NonComponent2(perInstanceComponent);
    }
}
