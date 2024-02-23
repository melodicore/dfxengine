package me.datafox.dfxengine.injector.test.classes.fail.array.constructor;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ConstructorArrayComponent {
    @Inject
    private ConstructorArrayComponent(String[] array) {

    }
}
