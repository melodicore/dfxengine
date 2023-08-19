package me.datafox.dfxengine.injector.test.injector.pass.per_instance;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class RequestingComponent {
    @Getter
    private final PerInstanceComponent perInstanceComponent;

    @Inject
    private RequestingComponent(PerInstanceComponent perInstanceComponent) {
        this.perInstanceComponent = perInstanceComponent;
    }
}
