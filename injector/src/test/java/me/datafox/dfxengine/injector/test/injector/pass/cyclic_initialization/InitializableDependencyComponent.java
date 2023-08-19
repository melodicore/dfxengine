package me.datafox.dfxengine.injector.test.injector.pass.cyclic_initialization;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

/**
 * @author datafox
 */
@Component
public class InitializableDependencyComponent {
    @Getter
    private OtherDependencyComponent otherDependencyComponent;

    @Initialize
    private void initialize(OtherDependencyComponent otherDependencyComponent) {
        this.otherDependencyComponent = otherDependencyComponent;
    }
}
