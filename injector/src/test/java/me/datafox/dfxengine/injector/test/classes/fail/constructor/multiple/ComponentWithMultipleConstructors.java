package me.datafox.dfxengine.injector.test.classes.fail.constructor.multiple;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ComponentWithMultipleConstructors {
    @Inject
    private ComponentWithMultipleConstructors(Component1 c1) {

    }
    @Inject
    private ComponentWithMultipleConstructors(Component2 c2) {

    }
}
