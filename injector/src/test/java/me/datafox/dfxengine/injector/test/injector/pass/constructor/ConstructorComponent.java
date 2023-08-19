package me.datafox.dfxengine.injector.test.injector.pass.constructor;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ConstructorComponent {
    @Getter
    private final ConstructorComponentDependency constructorComponentDependency;

    @Inject
    private ConstructorComponent(ConstructorComponentDependency constructorComponentDependency) {
        this.constructorComponentDependency = constructorComponentDependency;
    }

}
