package me.datafox.dfxengine.injector.test.classes.pass.per_instance;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Getter
@Component
public class RequestingComponent {
    private final PerInstanceComponent picc;

    @Inject
    private RequestingComponent(PerInstanceComponent perInstanceComponent) {
        this.picc = perInstanceComponent;
    }
}
