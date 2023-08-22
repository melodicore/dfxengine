package me.datafox.dfxengine.injector.test.injector.pass.multiple_equals;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class EqualsClassInitializer {
    @Component
    private EqualsClass buildFirst() {
        return new EqualsClass();
    }

    @Component
    private EqualsClass buildSecond() {
        return new EqualsClass();
    }
}
