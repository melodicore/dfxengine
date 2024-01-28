package me.datafox.dfxengine.injector.test.injector.warn.parameterized;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ParameterizedComponentDependency {
    @Inject
    public ParameterizedComponentDependency(ParameterizedComponent<String> parameterizedComponent) {

    }
}
