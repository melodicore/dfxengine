package me.datafox.dfxengine.injector.test.classes.pass.array;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class ObjectArrayMethodComponent {
    @Component
    String[] intArrayComponent() {
        return new String[] { "one", "two" };
    }
}
