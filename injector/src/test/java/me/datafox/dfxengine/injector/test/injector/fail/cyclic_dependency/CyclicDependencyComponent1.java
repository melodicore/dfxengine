package me.datafox.dfxengine.injector.test.injector.fail.cyclic_dependency;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class CyclicDependencyComponent1 {
    @Getter
    private final CyclicDependencyComponent2 cyclicDependencyComponent2;

    @Inject
    private CyclicDependencyComponent1(CyclicDependencyComponent2 cyclicDependencyComponent2) {
        this.cyclicDependencyComponent2 = cyclicDependencyComponent2;
    }
}
