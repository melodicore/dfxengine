package me.datafox.dfxengine.injector.test.injector.fail.cyclic_dependency;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class CyclicDependencyComponent2 {
    @Getter
    private final CyclicDependencyComponent1 cyclicDependencyComponent1;

    @Inject
    private CyclicDependencyComponent2(CyclicDependencyComponent1 cyclicDependencyComponent1) {
        this.cyclicDependencyComponent1 = cyclicDependencyComponent1;
    }
}
