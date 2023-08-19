package me.datafox.dfxengine.injector.test.injector.pass.combined;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class CombinedComponent {
    @Getter
    private final CombinedComponentConstructorDependency combinedComponentConstructorDependency;

    @Getter
    @Inject
    private CombinedComponentFieldDependency combinedComponentFieldDependency;

    @Inject
    private CombinedComponent(CombinedComponentConstructorDependency combinedComponentConstructorDependency) {
        this.combinedComponentConstructorDependency = combinedComponentConstructorDependency;
    }
}
