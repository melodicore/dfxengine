package me.datafox.dfxengine.injector.test.injector.pass.cyclic_initialization;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class OtherDependencyComponent {
    @Getter
    private final InitializableDependencyComponent initializableDependencyComponent;

    @Inject
    private OtherDependencyComponent(InitializableDependencyComponent initializableDependencyComponent) {
        this.initializableDependencyComponent = initializableDependencyComponent;
    }
}
