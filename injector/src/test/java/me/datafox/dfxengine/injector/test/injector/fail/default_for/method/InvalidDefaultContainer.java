package me.datafox.dfxengine.injector.test.injector.fail.default_for.method;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.test.injector.fail.default_for.NonInheritedClass;

/**
 * @author datafox
 */
@Component(defaultFor = NonInheritedClass.class)
public class InvalidDefaultContainer {
    @Component(defaultFor = NonInheritedClass.class)
    public InvalidDefaultNonComponent getInvalidDefaultNonComponent() {
        return new InvalidDefaultNonComponent();
    }
}
