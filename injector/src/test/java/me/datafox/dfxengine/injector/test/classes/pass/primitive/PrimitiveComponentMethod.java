package me.datafox.dfxengine.injector.test.classes.pass.primitive;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class PrimitiveComponentMethod {
    @Component
    int getInt() {
        return 5;
    }
}
