package me.datafox.dfxengine.injector.test.classes.fail.dependency.invalid;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ComponentWithInvalidDependency {
    @Inject
    private ComponentWithInvalidDependency(String str) {

    }
}
