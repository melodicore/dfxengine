package me.datafox.dfxengine.injector.test.injector.warn.parameterized;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

/**
 * @author datafox
 */
@Component
public class ParameterizedComponentInitializer {
    @Initialize
    public void initialize(ParameterizedComponent<String> parameterizedComponent) {

    }
}
