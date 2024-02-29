package me.datafox.dfxengine.injector.test.classes.pass.method_field;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
public class MethodComponent {
    @Inject
    private Component4 component4;

    @Component
    private NonComponent4 getNonComponent() {
        return new NonComponent4(component4);
    }
}
