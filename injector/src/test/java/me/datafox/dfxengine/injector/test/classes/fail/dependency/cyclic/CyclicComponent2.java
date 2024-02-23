package me.datafox.dfxengine.injector.test.classes.fail.dependency.cyclic;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class CyclicComponent2 {
    @Inject
    private CyclicComponent3 cyclicComponent3;
}
