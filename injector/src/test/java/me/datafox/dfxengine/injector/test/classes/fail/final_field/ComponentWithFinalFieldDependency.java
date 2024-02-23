package me.datafox.dfxengine.injector.test.classes.fail.final_field;

import me.datafox.dfxengine.injector.api.InstantiationPolicy;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ComponentWithFinalFieldDependency {
    @Inject
    private final InstantiationPolicy instantiationPolicy = null;
}
