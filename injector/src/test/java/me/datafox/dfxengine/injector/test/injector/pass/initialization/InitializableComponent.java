package me.datafox.dfxengine.injector.test.injector.pass.initialization;

import lombok.Getter;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

/**
 * @author datafox
 */
@Component
public class InitializableComponent {
    @Getter
    private String string;

    private InitializableComponent() {
        string = null;
    }

    @Initialize
    private void initialize() {
        string = "test";
    }
}
