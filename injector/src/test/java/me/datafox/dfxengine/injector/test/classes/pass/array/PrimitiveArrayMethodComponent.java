package me.datafox.dfxengine.injector.test.classes.pass.array;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class PrimitiveArrayMethodComponent {
    @Component
    int[] intArrayComponent() {
        return new int[] { 1, 2 };
    }
}
