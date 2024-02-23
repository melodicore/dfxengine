package me.datafox.dfxengine.injector.test.classes.fail.constructor.none;

import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
@Component
public class ComponentWithNoConstructor {
    private ComponentWithNoConstructor(String str) {

    }
}
