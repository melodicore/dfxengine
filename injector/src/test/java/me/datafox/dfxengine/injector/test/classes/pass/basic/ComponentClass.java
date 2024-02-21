package me.datafox.dfxengine.injector.test.classes.pass.basic;

import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;

/**
 * @author datafox
 */
@Component
public class ComponentClass {
    @Inject
    private NonComponentClass nonComponentClass;

    @Inject
    private ComponentClass(ComponentMethodClass componentMethodClass) {

    }
}
