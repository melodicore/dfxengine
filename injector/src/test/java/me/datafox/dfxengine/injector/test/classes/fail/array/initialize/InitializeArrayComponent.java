package me.datafox.dfxengine.injector.test.classes.fail.array.initialize;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Initialize;

/**
 * @author datafox
 */
@Component
public class InitializeArrayComponent {
    @Initialize
    private void initialize(String[] array) {

    }
}
