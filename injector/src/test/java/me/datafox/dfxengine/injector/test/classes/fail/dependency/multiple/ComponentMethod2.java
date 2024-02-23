package me.datafox.dfxengine.injector.test.classes.fail.dependency.multiple;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */

public class ComponentMethod2 {
    @Component
    private String getString1() {
        return "one";
    }
    @Component
    private String getString2() {
        return "two";
    }
    @Component
    private Double getDouble(String str) {
        return 5d;
    }
}
