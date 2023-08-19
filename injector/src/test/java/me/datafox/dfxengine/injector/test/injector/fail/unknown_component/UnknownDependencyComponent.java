package me.datafox.dfxengine.injector.test.injector.fail.unknown_component;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class UnknownDependencyComponent {
    @Getter
    private final NotAComponent notAComponent;

    @Inject
    private UnknownDependencyComponent(NotAComponent notAComponent) {
        this.notAComponent = notAComponent;
    }
}
