package me.datafox.dfxengine.injector.test.classes.pass.void_component;

import me.datafox.dfxengine.injector.api.InstantiationDetails;
import me.datafox.dfxengine.injector.api.annotation.Component;

/**
 * @author datafox
 */
public class VoidComponentMethod {
    @Component
    public void voidComponent(InstantiationDetails details) {
    }
}
